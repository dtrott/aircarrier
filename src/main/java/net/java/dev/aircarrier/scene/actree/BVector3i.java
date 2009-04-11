package net.java.dev.aircarrier.scene.actree;

import com.jme.math.Vector3f;

public class BVector3i implements Vector3i {

	int[] coords;

	public BVector3i() {
		super();
		coords = new int[]{0, 0, 0};
	}

	public BVector3i(int x, int y, int z) {
		super();
		coords = new int[]{x, y, z};
	}

	public BVector3i(Vector3i toClone) {
		this();
		set(toClone);
	}

	public BVector3i(Vector3f toClone) {
		this();
		set(toClone);
	}

	public int getX() {
		return coords[0];
	}

	public void set(int x, int y, int z) {
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
	}

	public void setX(int x) {
		coords[0] = x;
	}

	public int getY() {
		return coords[1];
	}

	public void setY(int y) {
		coords[1] = y;
	}

	public int getZ() {
		return coords[2];
	}

	public void setZ(int z) {
		coords[2] = z;
	}

	public int get(int i) {
		return coords[i];
	}

	public BVector3i addLocalMult(Vector3i toAdd, int mult) {
		for (int i = 0; i < 3; i++) {
			coords[i] += toAdd.get(i) * mult;
		}
		return this;
	}

	public BVector3i addLocal(Vector3i toAdd) {
		for (int i = 0; i < 3; i++) {
			coords[i] += toAdd.get(i);
		}
		return this;
	}

	public BVector3i subtractLocal(Vector3i toAdd) {
		for (int i = 0; i < 3; i++) {
			coords[i] -= toAdd.get(i);
		}
		return this;
	}
	
	public BVector3i multLocal(Vector3i toAdd) {
		for (int i = 0; i < 3; i++) {
			coords[i] *= toAdd.get(i);
		}
		return this;
	}

	public BVector3i divideLocal(Vector3i toDivide) {
		for (int i = 0; i < 3; i++) {
			coords[i] /= toDivide.get(i);
		}
		return this;
	}
	
	public int lengthSquared() {
		int ls = 0;
		for (int i = 0; i < 3; i++) {
			ls += coords[i] * coords[i]; 
		}
		return ls;
	}

	public void set(int i, int val) {
		coords[i] = val;
	}

	public void set(Vector3i toSet) {
		for (int i = 0; i < 3; i++) {
			coords[i] = toSet.get(i);
		}		
	}

	public void set(Vector3f toSet) {
		for (int i = 0; i < 3; i++) {
			coords[i] = Math.round(toSet.get(i));
		}		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		
		if (obj instanceof Vector3i) {
			Vector3i v = (Vector3i) obj;
			return (v.getX() == getX() && v.getY() == getY() && v.getZ() == getZ());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getX() + getY() * 31 + getZ() * 19;
	}

	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
	}
}
