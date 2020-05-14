package twogroups;
import java.awt.Color;
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
import utils.Point;

public class Simulation extends JPanel {
	LinkedList<Point> points = new LinkedList<Point>();
	NeuralNetwork nn;
	BufferedImage image;

	public Simulation() {
		//Constants.DEBUG=true;
		nn = new NeuralNetwork(2, 1, new Random(-1, 1), new Zeros(), new Logistic(), new MSE());
		nn.addLayer(new FullyConnectedLayer(15, new Random(-1, 1), new Zeros(), new Logistic()));
		//nn.addLayer(new FullyConnectedLayer(15, new Random(-1, 1), new Zeros(), new Logistic()));
		//nn.addLayer(new FullyConnectedLayer(15, new Random(-1, 1), new Zeros(), new Logistic()));
		nn.initialize();
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					points.add(new Point(e.getX(), e.getY(), 0));
				} else {
					points.add(new Point(e.getX(), e.getY(),1));
				}
				Simulation.this.repaint();
				super.mouseClicked(e);
			}

		});
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode() == KeyEvent.VK_S) {
					train();
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
			Tensor t=new Tensor(
					new double[][] { { p.getX() / (double) getWidth() }, { p.getY() / (double) getHeight() } });
			t.print();
			elem.setInput(t);
			
			elem.setDesired(new Tensor(new double[][] { { p.getColorValue()} }));
			elem.getDesired().print();
			d.getData().add(elem);
			
		}
		return d;

	}

	public void train() {
		//Constants.DEBUG=true;
		Dataset d = createDataset();
		SGD s=new SGD();
		
		s.setLearningRateWeights(0.05d);
		s.setLearningRateBiases(0.08d);
		NetworkTask task = nn.train(d, 4000, 1, s);
		NetworkMonitor m=(new NetworkMonitor() {

			@Override
			public void onIteration() {

			}

			@Override
			public void onFinish() {
				Constants.DEBUG=false;
				image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				for (int i = 0; i < getWidth(); i++) {
					for (int j = 0; j < getHeight(); j++) {
						Tensor output = nn.feedForward(new Tensor(
								new double[][] { { i / (double) getWidth() }, { j / (double) getHeight() } }));
						
						double v = output.getComponents()[0].getComponents()[0].getValue();
						
					
						image.setRGB(i, j, new Color(0,(int)((v/2f+0.5f)*255),(int)((1-v)*255)).getRGB());
					
						
						
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
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(image!=null) {
			
			g.drawImage(image, 0, 0, null);
		}
		for (Point p : points) {
			g.setColor(new Color(0,(int)((p.getColorValue()/2f+0.5f)*255),(int)((1-p.getColorValue())*255)));
			g.fillOval(p.getX(), p.getY(), 20, 20);
		}
	
	}
}
