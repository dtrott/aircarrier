package net.java.dev.aircarrier.ai.targetting;

/**
 * Combines by taking minimum
 * @author shingoki
 */
public class MaximumCombiner implements Combiner {
	public float combine(float a, float b) {
		return Math.min(a, b);
	}
}
