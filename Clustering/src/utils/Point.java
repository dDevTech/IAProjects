package utils;
import java.awt.Color;

public class Point {
	private int x;
	private int y;
	private double colorValue;
	private Color color;
	public Point(int x,int y,double colorValue) {
		this.x = x;
		this.y = y;
		this.colorValue = colorValue;
		
	}
	public Point(int x,int y,Color color) {
		this.x = x;
		this.y = y;
		this.setColor(color);
		
	}
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getColorValue() {
		return colorValue;
	}

	public void setColorValue(double colorValue) {
		this.colorValue = colorValue;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
}
