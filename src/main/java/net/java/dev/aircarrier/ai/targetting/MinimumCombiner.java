package net.java.dev.aircarrier.ai.targetting;

/**
 * Combines by taking maximum
 * @author shingoki
 */
public class MinimumCombiner implements Combiner {
	public float combine(float a, float b) {
		return Math.max(a, b);
	}
}
