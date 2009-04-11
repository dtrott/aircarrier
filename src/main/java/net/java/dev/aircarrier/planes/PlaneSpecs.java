package net.java.dev.aircarrier.planes;

public interface PlaneSpecs {

	public float getMinSpeed();
	public float getMidSpeed();
	public float getMaxSpeed();
	
	public float[] getAxisRates();
	
	public float getSlideHalfLife();

	public float getThrottleSpringK();
	public float getThrottleDampingK();

}
