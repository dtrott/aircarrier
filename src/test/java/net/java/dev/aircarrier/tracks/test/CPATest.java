/**
 * 
 */
package net.java.dev.aircarrier.tracks.test;

import net.java.dev.aircarrier.tracks.ClosestPointOfApproach;

import com.jme.math.Line;
import com.jme.math.Vector3f;

import junit.framework.TestCase;

/**
 * @author goki
 *
 */
public class CPATest extends TestCase {

	Line l1;
	Line l2;
	Line l3;
	Line l4;
	Line l5;

	Line s1;
	Line s2;

	Vector3f origin;
	Vector3f oneX;
	Vector3f oneY;
	Vector3f oneZ;
	Vector3f oneYoneZ;
	Vector3f twoAndAHalfX;
	
	public final static float DELTA = 0.0000001f;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		origin = new Vector3f(0, 0, 0);
		oneX = new Vector3f(1, 0, 0);
		oneY = new Vector3f(0, 1, 0);
		oneZ = new Vector3f(0, 0, 1);
		
		oneYoneZ = new Vector3f(0, 1, 1);

		twoAndAHalfX = new Vector3f(2.5f, 0, 0);

		l1 = new Line(origin, oneX);
		l2 = new Line(oneZ, oneY);
		l3 = new Line(oneYoneZ, oneY);
		l4 = new Line(oneZ, oneX);
		l5 = new Line(oneYoneZ, oneX);
		
		s1 = new Line(origin, oneX);
		s2 = new Line(twoAndAHalfX, oneX);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link net.java.dev.aircarrier.tracks.ClosestPointOfApproach#distanceBetweenTracks(com.jme.math.Line, com.jme.math.Line, float)}.
	 */
	public void testDistanceBetweenTracks() {
		//Lines crossing perpendicular, starting one unit apart and moving further. Should have min dist 1 at time 0
		assertEquals(1, ClosestPointOfApproach.distanceBetweenTracks(l1, l2, 0), DELTA);
		//At time 1, distance will be sqrt(1^2 + 1^2 + 1^2)
		assertEquals(Math.sqrt(3), ClosestPointOfApproach.distanceBetweenTracks(l1, l2, 1), DELTA);
	}

	/**
	 * Test method for {@link net.java.dev.aircarrier.tracks.ClosestPointOfApproach#distanceOfTrackClosestApproach(com.jme.math.Line, com.jme.math.Line)}.
	 */
	public void testDistanceOfTrackClosestApproach() {
		//Lines crossing perpendicular, starting one unit apart and moving further. Should have min dist 1 at time 0
		assertEquals(1, ClosestPointOfApproach.distanceOfTrackClosestApproach(l1, l2), DELTA);
		
		//Same but starting root 2 apart - however time of closest approach is half a second back, distance is root 1.5
		assertEquals(Math.sqrt(1.5f), ClosestPointOfApproach.distanceOfTrackClosestApproach(l1, l3), DELTA);		
		
		//Tracks travelling parallel at distance 1
		assertEquals(1, ClosestPointOfApproach.distanceOfTrackClosestApproach(l1, l4), DELTA);

		//Tracks travelling parallel at distance root 2
		assertEquals(Math.sqrt(2), ClosestPointOfApproach.distanceOfTrackClosestApproach(l1, l5), DELTA);

		//Try previous tracks backwards
		assertEquals(1, ClosestPointOfApproach.distanceOfTrackClosestApproach(l2, l1), DELTA);
		assertEquals(Math.sqrt(1.5f), ClosestPointOfApproach.distanceOfTrackClosestApproach(l3, l1), DELTA);		
		assertEquals(1, ClosestPointOfApproach.distanceOfTrackClosestApproach(l4, l1), DELTA);
		assertEquals(Math.sqrt(2), ClosestPointOfApproach.distanceOfTrackClosestApproach(l5, l1), DELTA);
		
		//Tracks have closest approach 0 to themselves at time 0 (as good as any)
		assertEquals(0, ClosestPointOfApproach.distanceOfTrackClosestApproach(l1, l1), DELTA);
		assertEquals(0, ClosestPointOfApproach.distanceOfTrackClosestApproach(l2, l2), DELTA);
		assertEquals(0, ClosestPointOfApproach.distanceOfTrackClosestApproach(l3, l3), DELTA);
		assertEquals(0, ClosestPointOfApproach.distanceOfTrackClosestApproach(l4, l4), DELTA);
		assertEquals(0, ClosestPointOfApproach.distanceOfTrackClosestApproach(l5, l5), DELTA);
	}

	/**
	 * Test method for {@link net.java.dev.aircarrier.tracks.ClosestPointOfApproach#timeOfTrackClosestApproach(com.jme.math.Line, com.jme.math.Line)}.
	 */
	public void testTimeOfTrackClosestApproach() {
		//Lines crossing perpendicular, starting one unit apart and moving further. Should have min dist at time 0
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l1, l2), DELTA);
		
		//Same but starting root 2 apart - however time of closest approach is half a second back.
		assertEquals(-0.5f, ClosestPointOfApproach.timeOfTrackClosestApproach(l1, l3), DELTA);		
		
		//Tracks travelling parallel - should give time 0 (as good as any)
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l1, l4), DELTA);
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l1, l5), DELTA);

		//Try previous tracks backwards
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l2, l1), DELTA);
		assertEquals(-0.5f, ClosestPointOfApproach.timeOfTrackClosestApproach(l3, l1), DELTA);		
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l4, l1), DELTA);
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l5, l1), DELTA);
		
		//Tracks have closest approach to themselves at time 0 (as good as any)
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l1, l1), DELTA);
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l2, l2), DELTA);
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l3, l3), DELTA);
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l4, l4), DELTA);
		assertEquals(0, ClosestPointOfApproach.timeOfTrackClosestApproach(l5, l5), DELTA);

	}

	
	/**
	 * Test method for {@link net.java.dev.aircarrier.tracks.ClosestPointOfApproach#closestApproachSegmentToSegment(com.jme.math.Line, com.jme.math.Line)}.
	 */
	public void testClosestApproachSegmentToSegment() {

		//Lines crossing perpendicular, but one unit apart. Check it DOES matter where
		//lines start - as infinite lines l2 and l3 are the same, but different as segments
		assertEquals(1, ClosestPointOfApproach.closestApproachSegmentToSegment(l1, l2), DELTA);
		assertEquals(Math.sqrt(2), ClosestPointOfApproach.closestApproachSegmentToSegment(l1, l3), DELTA);
		
		//Parallel lines one unit apart
		assertEquals(1, ClosestPointOfApproach.closestApproachSegmentToSegment(l1, l4), DELTA);
		
		//Parallel lines offset on unit in two axes - root 2 separation
		assertEquals(Math.sqrt(2), ClosestPointOfApproach.closestApproachSegmentToSegment(l1, l5), DELTA);

		//Same infinite lines, but as segments they are 1.5f apart (like a dashed line)
		assertEquals(1.5f, ClosestPointOfApproach.closestApproachSegmentToSegment(s1, s2), DELTA);

		//Try the previous ones backwards
		assertEquals(1, ClosestPointOfApproach.closestApproachSegmentToSegment(l2, l1), DELTA);
		assertEquals(Math.sqrt(2), ClosestPointOfApproach.closestApproachSegmentToSegment(l3, l1), DELTA);
		assertEquals(1, ClosestPointOfApproach.closestApproachSegmentToSegment(l4, l1), DELTA);
		assertEquals(Math.sqrt(2), ClosestPointOfApproach.closestApproachSegmentToSegment(l5, l1), DELTA);
		assertEquals(1.5f, ClosestPointOfApproach.closestApproachSegmentToSegment(s2, s1), DELTA);

		//Lines have 0 distance to each other
		assertEquals(0, ClosestPointOfApproach.closestApproachSegmentToSegment(l1, l1), DELTA);
		assertEquals(0, ClosestPointOfApproach.closestApproachSegmentToSegment(l2, l2), DELTA);
		assertEquals(0, ClosestPointOfApproach.closestApproachSegmentToSegment(l3, l3), DELTA);
		assertEquals(0, ClosestPointOfApproach.closestApproachSegmentToSegment(l4, l4), DELTA);
		assertEquals(0, ClosestPointOfApproach.closestApproachSegmentToSegment(l5, l5), DELTA);
	}

	
	/**
	 * Test method for {@link net.java.dev.aircarrier.tracks.ClosestPointOfApproach#closestApproachLineToLine(com.jme.math.Line, com.jme.math.Line)}.
	 */
	public void testClosestApproachLineToLine() {

		//Lines crossing perpendicular, but one unit apart. Check it doesn't matter where
		//lines start - as infinite lines l2 and l3 are the same
		assertEquals(1, ClosestPointOfApproach.closestApproachLineToLine(l1, l2), DELTA);
		assertEquals(1, ClosestPointOfApproach.closestApproachLineToLine(l1, l3), DELTA);
		
		//Parallel lines one unit apart
		assertEquals(1, ClosestPointOfApproach.closestApproachLineToLine(l1, l4), DELTA);
		
		//Parallel lines offset on unit in two axes - root 2 separation
		assertEquals(Math.sqrt(2), ClosestPointOfApproach.closestApproachLineToLine(l1, l5), DELTA);

		//Same infinite lines, distance 0
		assertEquals(0, ClosestPointOfApproach.closestApproachLineToLine(s1, s2), DELTA);
		
		//Try the previous ones backwards
		assertEquals(1, ClosestPointOfApproach.closestApproachLineToLine(l2, l1), DELTA);
		assertEquals(1, ClosestPointOfApproach.closestApproachLineToLine(l3, l1), DELTA);
		assertEquals(1, ClosestPointOfApproach.closestApproachLineToLine(l4, l1), DELTA);
		assertEquals(Math.sqrt(2), ClosestPointOfApproach.closestApproachLineToLine(l5, l1), DELTA);
		assertEquals(0, ClosestPointOfApproach.closestApproachLineToLine(s2, s1), DELTA);

		//Lines have 0 distance to each other
		assertEquals(0, ClosestPointOfApproach.closestApproachLineToLine(l1, l1), DELTA);
		assertEquals(0, ClosestPointOfApproach.closestApproachLineToLine(l2, l2), DELTA);
		assertEquals(0, ClosestPointOfApproach.closestApproachLineToLine(l3, l3), DELTA);
		assertEquals(0, ClosestPointOfApproach.closestApproachLineToLine(l4, l4), DELTA);
		assertEquals(0, ClosestPointOfApproach.closestApproachLineToLine(l5, l5), DELTA);

	}

}
