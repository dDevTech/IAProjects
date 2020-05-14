package project;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import core.Tensor;
import neuralnetwork.NeuralNetwork;

public abstract class DrawPanel extends JPanel{
	boolean paint=false;
	int prevX=-1;
	int prevY=-1;
	int x=0;
	int y=0;
	private Graphics2D g2;
	ArrayList<Line>lines= new ArrayList<Line>();
	BufferedImage bf;
	public abstract void onDraw(Tensor input,Tensor output);
	public DrawPanel(NeuralNetwork n) {
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				paint=false;
				prevX=-1;
				prevY=-1;
				super.mouseReleased(e);
				int c=0;
				
				Image tmp= bf.getScaledInstance(28, 28, BufferedImage.SCALE_FAST);
			 	BufferedImage dimg = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);	
				Graphics2D g2d = dimg.createGraphics();
			    g2d.drawImage(tmp, 0, 0, null);
			    g2d.dispose();
			    Tensor[] components=new Tensor[dimg.getWidth()*dimg.getHeight()];
				
				for(int j=0;j<dimg.getHeight();j++) {
					for(int i=0;i<dimg.getWidth();i++) {
						Color col= new Color(dimg.getRGB(i, j));
						components[c]= new Tensor(new Tensor[] {new Tensor(col.getRed()/255d)});
						c++;
					}
				}
				Tensor k= new Tensor(components);
				n.feedForward(k);
				onDraw(k,n.getOutputLayer().getOutput());
			}
			
			
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
				paint=true;
				x=e.getX();
				y=e.getY();
				
			}
		});
		setPaint();
		
	}
	public void setPaint() {
		bf= new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		g2=bf.createGraphics();
		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(18f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2.setColor(new Color(0,0,0,0));
		g2.fillRect ( 0, 0, bf.getWidth(), bf.getHeight() );
	}
	public void paint(Graphics g) {
		
		super.paint(g);
	
		
		if(paint) {
			
			if(prevX!=-1) {
				g2.setColor(Color.white);
				g2.drawLine(prevX, prevY, x, y);
			}
			prevX=x;
			prevY=y;
		}
		g.drawImage(bf, 0,0,null);
	
		repaint();
		
	}

}
