package net.java.dev.aircarrier.controls;

import net.java.dev.aircarrier.ai.ScalarSensor;

/**
 * A set of controls that also has a sensor output indicating how strongly the controls
 * believe they need to be applied
 * @author shingoki
 */
public interface PlaneControlsWithSensor extends PlaneControls, ScalarSensor {

}
