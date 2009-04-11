package net.java.dev.aircarrier.hud;

/**
 * A gauge has a settable value
 * @author goki
 */
public interface Gauge {

	/**
	 * @return
	 * 		Current gauge value
	 */
	public float getValue();
	
	/**
	 * Set a new gauge value
	 * @param value
	 */
	public void setValue(float value);
	
}
