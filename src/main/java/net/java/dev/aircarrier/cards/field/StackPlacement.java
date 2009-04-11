package net.java.dev.aircarrier.cards.field;

import net.java.dev.aircarrier.cards.stack.Stack;

public class StackPlacement implements Comparable<StackPlacement>{

	Stack stack;
	int x;
	int y;
	int z;
	
	public StackPlacement(Stack stack) {
		super();
		this.stack = stack;
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

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public Stack getStack() {
		return stack;
	}
	
	@Override
	public int compareTo(StackPlacement o) {
		int zCompare = new Integer(getZ()).compareTo(o.getZ());
		if (zCompare != 0) return zCompare;
		return getStack().getName().compareTo(o.getStack().getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof StackPlacement) {
			StackPlacement other = (StackPlacement) obj;
			return (compareTo(other) == 0);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return getZ() + 31 * getStack().getName().hashCode();
	}
	
}
