package net.java.dev.aircarrier.ai.targetting;

public interface Combiner {

	public final static Combiner MINIMUM = new MinimumCombiner();
	public final static Combiner MAXIMUM = new MaximumCombiner();
	public final static Combiner ADDITION = new AdditionCombiner();
	public final static Combiner MULTIPLICATION = new MultiplicationCombiner();
	
	public float combine(float a, float b);
	
}
