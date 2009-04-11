package net.java.dev.aircarrier.scene.actree;

import com.jme.math.Vector2f;

public interface Vector2i {

	public int getX();

	public void setX(int x);

	public int getY();

	public void setY(int y);

	public int get(int i);

	public void set(int i, int val);

	public void setComponents(int x, int y);

	public Vector2i addLocal(Vector2i toAdd);

	public Vector2i subtractLocal(Vector2i toAdd);

	public Vector2i multLocal(Vector2i toAdd);

	public Vector2i divideLocal(Vector2i toAdd);

	public int lengthSquared();

	public void set(Vector2i toSet);

	public void set(Vector2f toSet);

}
