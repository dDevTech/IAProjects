package moregroups;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Window2 extends JFrame{
	
	public Window2(int width,int height) {
		if(width==-1||height==-1) {
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setUndecorated(true);
		}else {
			this.setSize(width,height);
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Clustering");
		this.setLocationRelativeTo(null);
		this.setContentPane(new Simulation2());
	}
}
