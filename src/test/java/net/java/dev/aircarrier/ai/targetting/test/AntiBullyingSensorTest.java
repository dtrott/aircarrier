/**
 * 
 */
package net.java.dev.aircarrier.ai.targetting.test;


import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.acobject.DummyAcobject;
import net.java.dev.aircarrier.ai.targetting.AntiBullyingSensor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author shingoki
 *
 */
public class AntiBullyingSensorTest {

	private static double DELTA = 0.000001;
	
	Acobject a;
	Acobject b;
	Acobject c;
	Acobject d;
	AntiBullyingSensor sensor;
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
		sensor = new AntiBullyingSensor(0, 3);
	}

	@Test public void testSensor() {

		//All prey should have value 1 to all hunters
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
			}
		}
		
		//Target a onto b
		sensor.targettingChange(a, null, b);
		
		//everything but b should have value 1 to all hunters still, 
		//b should have lower value to all hunters
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (prey == b) {
					//one hunter, value 2/3
					assertEquals(2d/3d, sensor.getTargettingValue(hunter, prey), DELTA);					
				} else {
					assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}

		//Target c onto b
		sensor.targettingChange(c, null, b);
		
		//everything but b should have value 1 to all hunters still, 
		//b should have lower value to all hunters
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (prey == b) {
					//2 hunters, value 1/3
					assertEquals(1d/3d, sensor.getTargettingValue(hunter, prey), DELTA);					
				} else {
					assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}

		//Target d onto b
		sensor.targettingChange(d, null, b);
		
		//everything but b should have value 1 to all hunters still, 
		//b should have lower value to all hunters
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (prey == b) {
					//3 hunters, so value 0
					assertEquals(0, sensor.getTargettingValue(hunter, prey), DELTA);					
				} else {
					assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}

		//Target d onto a
		sensor.targettingChange(d, b, a);
		
		//everything but a and b should have value 1 to all hunters still, 
		//a and b should have lower value to all hunters.
		//Since d has been retargetted from b to a, a should have 2 hunters, so value 1/3
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (prey == a) {
					//One hunter, 2/3
					assertEquals(2d/3d, sensor.getTargettingValue(hunter, prey), DELTA);
				} else if (prey == b) {
					//Two hunters (one has changed to a), 1/3
					assertEquals(1d/3d, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
				}
			}
		}
		
		//Target d from a to null
		sensor.targettingChange(d, a, null);
		
		//As before, but a has no hunters now, so should be back to value 1
		for (Acobject hunter : all) {
			for (Acobject prey : all) {
				if (prey == b) {
					//Two hunters (one has changed to a), 1/3
					assertEquals(1d/3d, sensor.getTargettingValue(hunter, prey), DELTA);
				} else {
					assertEquals(1, sensor.getTargettingValue(hunter, prey), DELTA);
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
