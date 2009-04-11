package net.java.dev.aircarrier.util;

public class ExponentialDecay {

	//Contains an exponential decay from 1, with half life LUT_HALF_LIFE
	private static float[] LUT;
	//Contains gradient of each step in LUT
	private static float[] GRADIENT_LUT;
	//half life of the decay in the LUT
	private static float LUT_HALF_LIFE = 20;
	//Number of entries in the LUT
	private static int LUT_LENGTH = 200;
	//Max index in LUT
	private static int LUT_MAX_INDEX = LUT_LENGTH - 1;
	
	//Generate the LUT
	static {
		//Make basic LUT
		LUT = new float[LUT_LENGTH];
		for (int i = 0; i < LUT_LENGTH; i++) {
			LUT[i] = (float)Math.exp(i * Math.log(0.5) / LUT_HALF_LIFE);
		}
		
		//Make gradient LUT
		GRADIENT_LUT = new float[LUT_LENGTH];
		for (int i = 0; i < LUT_LENGTH - 1; i++) {
			GRADIENT_LUT[i] = LUT[i+1] - LUT[i]; 
		}
		//Zero gradient for last entry, since last entry is just set to 0 and has no next entry
		GRADIENT_LUT[LUT_LENGTH-1] = 0;
		
		//Make last LUT entry 0
		LUT[LUT_MAX_INDEX] = 0;
		//Make the gradient LUT
	}

	/**
	 * The time factor by which to multiply a time for evaluation, to
	 * yield a time in the same scale as the LUT (this corrects for difference
	 * between the half life of an instance, and the half life of the curve in the LUT)
	 */
	float timeFactor;
	
	/**
	 * Make an exponential decay
	 * @param halfLife
	 * 		The time for a quantity to decay to half its original size,
	 * 		i.e. the time for which evaluate(time) returns 0.5 (or close to that)
	 */
	public ExponentialDecay(float halfLife) {
		setHalfLife(halfLife);
	}

	/**
	 * Set half life.
	 * This causes a division.
	 * @param halfLife
	 * 		The time for a quantity to decay to half its original size,
	 * 		i.e. the time for which evaluate(time) returns 0.5 (or close to that)
	 */
	public void setHalfLife(float halfLife) {
		//Work out the factor from the half life we require, to the
		//fixed half life of the LUT.
		//This gets our division out of the way so we only have mults
		//for each evaluation
		timeFactor = LUT_HALF_LIFE / halfLife;		
	}
	
	/**
	 * Evaluate the decay factor over a given time, which must be non-negative
	 * @param time
	 * 		The time we are decaying for
	 * @return
	 * 		The factor of the original value that is left after decay. e.g.
	 * 		evaluate(halfLife) will return 0.5, meaning that over halfLife seconds,
	 * 		any original value will decay to half the value
	 */
	public float evaluate(float time) {
				
		//Move to LUT timescale using precalculated factor
		time *= timeFactor;
		
		//Look up in table as int
		int intTime = (int)time;
		
		//If we are off the end of LUT, return 0
		if (intTime > LUT_MAX_INDEX) return 0;
		
		//Look up in LUT
		float baseValue = LUT[intTime];

		
		float gradient = GRADIENT_LUT[intTime];
		float remainder = time - (float)intTime;
		float value = baseValue + gradient * remainder;
		return value;
		
		//return baseValue;
	}
	
	public final static void main(String[] args) {
		ExponentialDecay decay = new ExponentialDecay(30);
		/*
		for (int i = 0; i < 30; i++) {
			System.out.println(i + "\t" + decay.evaluate(i));
		}
		*/
		
		long start = System.nanoTime();
		for (int i = 0; i < 10000000; i++) {
			decay.evaluate(0.5f);
			decay.evaluate(20.5f);
			decay.evaluate(33f);
			decay.evaluate(201.563f);
			decay.evaluate(33.234563f);
			decay.evaluate(2.53463f);
			decay.evaluate(54.5333f);
			decay.evaluate(62.533453f);
			decay.evaluate(19.723f);
			decay.evaluate(23.333f);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1000000f;
		System.out.println("LUT time for 10000000 " + time + "ms");
		
		
		start = System.nanoTime();
		for (int i = 0; i < 10000000; i++) {
			Math.exp(0.5f);
			Math.exp(20.5f);
			Math.exp(33f);
			Math.exp(201.563f);
			Math.exp(33.234563f);
			Math.exp(2.53463f);
			Math.exp(54.5333f);
			Math.exp(62.533453f);
			Math.exp(19.723f);
			Math.exp(23.333f);
		}
		end = System.nanoTime();
		time = (end - start) / 1000000f;
		System.out.println("Exp time for 10000000 " + time + "ms");

	}
	
}
