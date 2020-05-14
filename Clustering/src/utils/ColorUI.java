package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ColorUI {
	private int x;
	private int y;
	private Color c;
	private char key;
	private boolean selected=false;
	public ColorUI(int x,int y,Color c,char key) {
		this.x = x;
		this.y = y;
		this.c = c;
		this.key = key;
		
	}
	public void paint(Graphics2D g) {
		g.setFont(new Font("Noto Sans",Font.BOLD,16));
		g.setColor(c);
		g.fillRect(x, y, 40, 40);
		if(!isSelected()) 
		g.setStroke(new BasicStroke(2f));
		else
		g.setStroke(new BasicStroke(4f));
		g.setColor(Color.lightGray); 
		g.drawRect(x, y, 40, 40);
		g.setColor(Color.white);
		g.drawString(Character.toString(key), x+10, y+20);
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
