package net.java.dev.aircarrier.pqsolver;

public class Swap {

	int x1, y1, x2, y2;

	public Swap(int x1, int y1, int x2, int y2) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public int getX1() {
		return x1;
	}

	public int getX2() {
		return x2;
	}

	public int getY1() {
		return y1;
	}

	public int getY2() {
		return y2;
	}

	public String toString() {
		return x1 + ", " + y1 + " to " + x2 + ", " + y2;
	}
	
}
