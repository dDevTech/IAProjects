package twogroups;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Window extends JFrame{
	
	public Window(int width,int height) {
		this.setSize(width,height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Clustering");
		this.setLocationRelativeTo(null);
		this.setContentPane(new Simulation());
	}
}
