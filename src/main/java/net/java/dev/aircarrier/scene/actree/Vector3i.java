package net.java.dev.aircarrier.scene.actree;

import com.jme.math.Vector3f;

public interface Vector3i {

	public int getX();

	public void setX(int x);

	public int getY();

	public void setY(int y);

	public int getZ();

	public void setZ(int z);

	public int get(int i);

	public void set(int i, int val);

	public void set(int x, int y, int z);

	public Vector3i addLocal(Vector3i toAdd);

	public Vector3i subtractLocal(Vector3i toAdd);

	public Vector3i multLocal(Vector3i toAdd);

	public Vector3i divideLocal(Vector3i toAdd);

	public int lengthSquared();

	public void set(Vector3i toSet);

	public void set(Vector3f toSet);

}
