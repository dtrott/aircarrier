package net.java.dev.aircarrier.ai.targetting;

/**
 * Combines by multiplication
 * @author shingoki
 */
public class MultiplicationCombiner implements Combiner {
	public float combine(float a, float b) {
		return a*b;
	}
}
