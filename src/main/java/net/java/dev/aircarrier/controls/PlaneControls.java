package net.java.dev.aircarrier.controls;

public interface PlaneControls extends SteeringControls {

	public abstract void setFiring(int gun, boolean firing);
	
	public boolean isFiring(int gun);
	
	public void clearFiring();

	public int gunCount();
	
}