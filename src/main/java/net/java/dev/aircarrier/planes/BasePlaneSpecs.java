package net.java.dev.aircarrier.planes;

public class BasePlaneSpecs implements PlaneSpecs {

	float[] axisRates;
	float minSpeed;
	float midSpeed; 
	float maxSpeed;
	float slideHalfLife;
	float throttleSpringK;
	float throttleDampingK;
	
	/**
	 * Create plane specs
	 * @param axisRates
	 * 		The maximum rate of rotation around each axis
	 * @param minSpeed
	 * 		The minimum speed corrresponding to full braking
	 * @param midSpeed
	 * 		The speed corresponding to neutral throttle position
	 * @param maxSpeed
	 * 		The maximum speed corresponding to full boost
	 * @param slideHalfLife
	 * 		The half life for velocity target seeking during sliding movements
	 * @param throttleSpringK
	 * 		The spring factor for "engine" speed tracking throttle position, that is,
	 * 		this influences the responsiveness of speed to throttle, independent of
	 * 		sliding
	 * @param throttleDampingK
	 * 		The spring factor for "engine" speed tracking throttle position, that is,
	 * 		this influences the responsiveness of speed to throttle, independent of
	 * 		sliding
	 */
	public BasePlaneSpecs(
			float[] axisRates, 
			float minSpeed, float midSpeed, float maxSpeed, 
			float slideHalfLife, 
			float throttleSpringK, float throttleDampingK) {
		super();
		this.axisRates = axisRates;
		this.minSpeed = minSpeed;
		this.midSpeed = midSpeed;
		this.maxSpeed = maxSpeed;
		this.slideHalfLife = slideHalfLife;
		this.throttleSpringK = throttleSpringK;
		this.throttleDampingK = throttleDampingK;
	}


	public float[] getAxisRates() {
		return axisRates;
	}


	public void setAxisRates(float[] axisRates) {
		this.axisRates = axisRates;
	}


	public float getMaxSpeed() {
		return maxSpeed;
	}


	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}


	public float getMidSpeed() {
		return midSpeed;
	}


	public void setMidSpeed(float midSpeed) {
		this.midSpeed = midSpeed;
	}


	public float getMinSpeed() {
		return minSpeed;
	}


	public void setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
	}

	public float getSlideHalfLife() {
		return slideHalfLife;
	}

	public void setSlideHalfLife(float slideHalfLife) {
		this.slideHalfLife = slideHalfLife;
	}


	public float getThrottleDampingK() {
		return throttleDampingK;
	}


	public void setThrottleDampingK(float throttleDampingK) {
		this.throttleDampingK = throttleDampingK;
	}


	public float getThrottleSpringK() {
		return throttleSpringK;
	}


	public void setThrottleSpringK(float throttleSpringK) {
		this.throttleSpringK = throttleSpringK;
	}

	

}
