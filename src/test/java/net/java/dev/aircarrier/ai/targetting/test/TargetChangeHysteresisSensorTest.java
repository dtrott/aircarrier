/**
 * 
 */
package net.java.dev.aircarrier.ai.targetting.test;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.acobject.DummyAcobject;
import net.java.dev.aircarrier.ai.targetting.TargetChangeHysteresisSensor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author shingoki
 *
 */
public class TargetChangeHysteresisSensorTest {

	private static double DELTA = 0.000001;
	
	Acobject a;
	Acobject b;
	Acobject c;
	Acobject d;
	TargetChangeHysteresisSensor sensor;
	List<Acobject> all;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		a = new DummyAcobject("a");
		b = new DummyAcobject("b");
		c = new DummyAcobject("c");
		d = new DummyAcobject("d");
		all = new ArrayList<Acobject>();
		all.add(a);
		all.add(b);
		all.add(c);
		all.add(d);
		sensor = new TargetChangeHysteresisSensor(3, 0.5f);
	}

	@Test public void testSensor() {

		//All prey should have value 0 to all hunters
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
			}
		}
		
		
		//Target a from null to b
		sensor.targettingChange(a, null, b);
		
		//0 values for everything except a targetting b, which should be 1
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (hunter == a && prey == b) {
					assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}
		
		//Watch decay of hysteresis value
		sensor.update(1);
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (hunter == a && prey == b) {
					assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}

		sensor.update(3);
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (hunter == a && prey == b) {
					assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}
		
		//Small update after the value should start to move below clipping to 1
		sensor.update(0.01f);
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (hunter == a && prey == b) {
					assertEquals(1-0.01*0.5, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}

		//Get to just before value hits 0
		sensor.update(1.98f);
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (hunter == a && prey == b) {
					assertEquals(0.01*0.5, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}
		
		//Check we stay clipped at 0
		sensor.update(1000f);
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (hunter == a && prey == b) {
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}

		//Change targetting of a to c, pass some time, change to d
		sensor.targettingChange(a, b, c);
		sensor.update(0.1f);
		sensor.targettingChange(a, c, d);
		sensor.update(0.1f);
		
		//We should now only have a value for a targetting d - nothing left on c, since it is lost when we switch target again
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (hunter == a && prey == d) {
					assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}
		
		//Add 0.4 seconds decay (in addition to the 0.1 we have had, this makes 0.5 => 0.25 extra decay),
		//this means that a's hysteresis on d should JUST clear in time
		//for the next check after we set up some other values
		sensor.update(0.4f);
		
		//Check that we have two different values for two hunters targetting the same prey
		//b targets d
		sensor.targettingChange(b, null, d);
		//one second extra decay on this
		sensor.update(1);
		//c targets d
		sensor.targettingChange(c, null, d);
		//Decay until c's hysteresis should be 0.75, and so b's should be 0.25
		sensor.update(2.25f*2f);

		//Check these two are the only values
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (hunter == b && prey == d) {
					assertEquals(0.25, sensor.getTargettingValue(hunter, prey), DELTA);
				} else if (hunter == c && prey == d) {
					assertEquals(0.75, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}

	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

}
