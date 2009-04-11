package net.java.dev.aircarrier.controls;

public interface SteeringControls {

	public final static int PITCH = 0;

	public final static int YAW = 1;

	public final static int ROLL = 2;

	public final static int THROTTLE = 3;

	public float getAxis(int axis);

	public void setAxis(int axis, float control);

	public void moveAxis(int axis, float control);

	public void update(float time);

}