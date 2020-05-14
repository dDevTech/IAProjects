package core;

public class Counter {
	private int counter;
	public void reset() {
		setCounter(0);
	}
	public void increment() {
		setCounter(getCounter() + 1);
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
}
