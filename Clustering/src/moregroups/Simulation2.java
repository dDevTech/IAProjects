package moregroups;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import core.Tensor;
import dataset.DataElement;
import dataset.Dataset;
import errors.CrossEntropy;
import errors.MSE;
import function.activation.Logistic;
import function.activation.Softmax;
import initializer.Random;
import initializer.Zeros;
import layers.FullyConnectedLayer;
import neuralnetwork.Constants;
import neuralnetwork.NetworkMonitor;
import neuralnetwork.NetworkTask;
import neuralnetwork.NeuralNetwork;
import optimization.SGD;
import utils.ColorUI;
import utils.Point;

public class Simulation2 extends JPanel {
	LinkedList<Point> points = new LinkedList<Point>();
	double error=0;
	double batchProgress=0;
	NeuralNetwork nn;
	BufferedImage image;
	Color[] listColors = new Color[] { new Color(0, 128, 255), new Color(0, 204, 0), new Color(153, 51, 255),
			new Color(255, 128, 0), new Color(0,204,204), new Color(204,0,204),new Color(204,0,0), new Color(102,102,255)};
	int selected = 0;
	ColorUI[]colorsUI= new ColorUI[listColors.length];
	boolean firstRender=true;
	public Simulation2() {
		this.setBackground(Color.black);
		// Constants.DEBUG=true;
		nn = new NeuralNetwork(2, 3, new Random(-1, 1), new Zeros(), new Logistic(), new MSE());
		nn.addLayer(new FullyConnectedLayer(15, new Random(-1, 1), new Zeros(), new Logistic()));
		nn.addLayer(new FullyConnectedLayer(15, new Random(-1, 1), new Zeros(), new Logistic()));
		// Logistic()));
		// nn.addLayer(new FullyConnectedLayer(15, new Random(-1, 1), new Zeros(), new
		// Logistic()));
		nn.initialize();
		
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (selected >= 0) {

					points.add(new Point(e.getX(), e.getY(), listColors[selected]));

					Simulation2.this.repaint();
				}

				super.mouseClicked(e);
			}

		});
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_R) {
					random();
				}
				if(e.getKeyCode()==KeyEvent.VK_C) {
					points.clear();
					image=null;
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_T) {
					train();
				}else {
					for(ColorUI c:colorsUI) {
						c.setSelected(false);
					}
				if (e.getKeyCode() == KeyEvent.VK_1)
					selected = 0;
				if (e.getKeyCode() == KeyEvent.VK_2)
					selected = 1;
				if (e.getKeyCode() == KeyEvent.VK_3)
					selected = 2;
				if (e.getKeyCode() == KeyEvent.VK_4)
					selected = 3;
				if (e.getKeyCode() == KeyEvent.VK_5)
					selected = 4;
				if (e.getKeyCode() == KeyEvent.VK_6)
					selected = 5;
				if (e.getKeyCode() == KeyEvent.VK_7)
					selected = 6;
				if (e.getKeyCode() == KeyEvent.VK_8)
					selected = 7;
				colorsUI[selected].setSelected(true);
				repaint();
				}
				
				super.keyPressed(e);
			}

		});
		this.setFocusable(true);
		this.requestFocus();
	}

	public Dataset createDataset() {
		Dataset d = new Dataset();
		for (Point p : points) {
			DataElement elem = new DataElement();
			Tensor t = new Tensor(
					new double[][] { { p.getX() / (double) getWidth() }, { p.getY() / (double) getHeight() } });
			t.print();
			elem.setInput(t);

			elem.setDesired(new Tensor(new double[][] { { p.getColor().getRed() / 255d },
					{ p.getColor().getGreen() / 255d }, { p.getColor().getBlue() / 255d } }));
			elem.getDesired().print();
			d.getData().add(elem);

		}
		return d;

	}
	public void random() {
		points.clear();
		int n=30;
		for(int i=0;i<n;i++) {
			int k=(int)(Math.random()*8);
			int x=(int)(Math.random()*getWidth());
			int y=(int)(Math.random()*getHeight());
			points.add(new Point(x,y,listColors[k]));
		}
		repaint();
		
	}

	public void train() {
		// Constants.DEBUG=true;
	
		Dataset d = createDataset();
		if(d.getData().isEmpty())return;
		SGD s = new SGD();

		s.setLearningRateWeights(0.05d);
		s.setLearningRateBiases(0.08d);
		
		NetworkTask task = nn.train(d, 3000, 1, s);
		NetworkMonitor m = (new NetworkMonitor() {

			@Override
			public void onIteration() {
				error=this.getError();
				batchProgress=this.getEpoch()/(double)this.getTotalEpochs();
				repaint();
			}

			@Override
			public void onFinish() {
				batchProgress=1;
				Constants.DEBUG = false;
				image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				for (int i = 0; i < getWidth(); i++) {
					for (int j = 0; j < getHeight(); j++) {
						Tensor output = nn.feedForward(new Tensor(
								new double[][] { { i / (double) getWidth() }, { j / (double) getHeight() } }));

						float r = (float) output.getComponents()[0].getComponents()[0].getValue();
						float g = (float) output.getComponents()[1].getComponents()[0].getValue();
						float b = (float) output.getComponents()[2].getComponents()[0].getValue();

						image.setRGB(i, j, new Color(r, g, b).getRGB());

					}
					repaint();
				}

			}
		});
		m.setPrintStatus(false);
		task.setMonitor(m);
		Thread t = new Thread(task);
		t.start();
	}

	public void paint(Graphics g) {
		super.paint(g);
		if(firstRender) {
			int j=10;
			int a=0;
			for(Color c: listColors) {
				ColorUI ui = new ColorUI(j, getHeight()-50, c, Character.forDigit(a+1, 10));
				colorsUI[a]=ui;
				j=j+50;
				a++;
			}
			firstRender=false;
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (image != null) {

			g.drawImage(image, 0, 0, null);
		}
		for (Point p : points) {
			g.setColor(p.getColor());
			g.fillOval(p.getX(), p.getY(), 20, 20);
		}
		for(ColorUI c:colorsUI) {
			c.paint(g2);
		}
		g.setFont(new Font("Noto Sans",Font.BOLD,16));
		g.setColor(Color.LIGHT_GRAY);
		g.drawString("Randomize Press R", 20, 20);
		g.drawString("Train and test press T", 20, 50);
		g.drawString("Click to add a point", 20, 80);
		g.drawString("C to clear canvas", 20, 110);
		g.drawString("Error: "+error, 20, 140);
		String prog=String.format("%.2g%n", batchProgress*100);
		g.drawString("Progress: "+prog+"%", 20, 170);
		
		

	}
}
