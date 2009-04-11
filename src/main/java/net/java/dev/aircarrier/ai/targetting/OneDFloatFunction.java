package net.java.dev.aircarrier.ai.targetting;

/**
 * A OneDFloatFunction just evaluates with a float input to give a float output
 * @author shingoki
 */
public interface OneDFloatFunction {
	/**
	 * Evaluate function
	 * @param value
	 * 		The input value
	 * @return
	 * 		The result value
	 */
	public float evaluate(float value);
}
