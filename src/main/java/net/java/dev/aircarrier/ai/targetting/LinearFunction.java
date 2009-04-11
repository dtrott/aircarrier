package net.java.dev.aircarrier.ai.targetting;

/**
 * A lienar function: output = offset + input * gradient
 * @author shingoki
 */
public class LinearFunction implements OneDFloatFunction {

	float gradient;
	float offset;
	
	/**
	 * Make a linear function
	 * @param gradient
	 * 		Gradient of output with input
	 * @param offset
	 * 		Offset, the value of output when input = 0
	 */
	public LinearFunction(float gradient, float offset) {
		super();
		this.gradient = gradient;
		this.offset = offset;
	}

	public float evaluate(float value) {
		return offset + value * gradient;
	}

}
