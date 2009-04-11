//Original Code:
//	 Copyright 2001, softSurfer (www.softsurfer.com)
//	 This code may be freely used and modified for any purpose
//	 providing that this copyright notice is included with it.
//	 SoftSurfer makes no warranty for this code, and cannot be held
//	 liable for any real or imagined damage resulting from its use.
//	 Users of this code must verify correctness for their application.

//Modifications:
/*
 *  $Id: ClosestPointOfApproach.java,v 1.2 2006/12/30 23:47:19 shingoki Exp $
 *
 * 	Copyright (c) 2005-2006 shingoki
 *
 *  This file is part of AirCarrier, see http://aircarrier.dev.java.net/
 *
 *    AirCarrier is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.

 *    AirCarrier is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.

 *    You should have received a copy of the GNU General Public License
 *    along with AirCarrier; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package net.java.dev.aircarrier.tracks;

import com.jme.math.FastMath;
import com.jme.math.Line;
import com.jme.math.Vector3f;

/**
 * Various calculations to do with closest approaches of lines (infinite), 
 * segments (one section of a line) and tracks (linear movement of a point with time).
 * 
 * @author goki
 *
 */
public class ClosestPointOfApproach {

	//Temp vectors
	private final static Vector3f u = new Vector3f();
	private final static Vector3f v = new Vector3f();
	private final static Vector3f w = new Vector3f();
	private final static Vector3f temp = new Vector3f();
	private final static Vector3f dv = new Vector3f();
	private final static Vector3f w0 = new Vector3f();    

	private final static float SMALL_NUM = 0.00000001f; // anything that avoids division overflow

	/**
	 * Calculate closest approach of two infinite lines (at one point in time)
	 * @param l1
	 * 		First line
	 * @param l2
	 * 		Second line
	 * @return
	 * 		The shortest distance between any pair of points on the two lines
	 */
	public static float closestApproachLineToLine(Line l1, Line l2) {
		return FastMath.sqrt(closestApproachLineToLineSquared(l1, l2));
	}

	/**
	 * Calculate closest approach (squared) of two infinite lines (at one point in time)
	 * @param l1
	 * 		First line
	 * @param l2
	 * 		Second line
	 * @return
	 * 		The shortest distance between any pair of points on the two lines
	 */
	public static float closestApproachLineToLineSquared(Line l1, Line l2) {

		//Vectors along the two lines
		u.set(l1.getDirection());
		v.set(l2.getDirection());
		
		//Vector from origin of second line to origin of first
		w.set(l1.getOrigin());
		w.subtractLocal(l2.getOrigin());
		
	    float    a = u.dot(u);        // always >= 0
	    float    b = u.dot(v);
	    float    c = v.dot(v);        // always >= 0
	    float    d = u.dot(w);
	    float    e = v.dot(w);
	    float    D = a*c - b*b;       // always >= 0
	    float    sc, tc;

	    // compute the line parameters of the two closest points
	    if (D < SMALL_NUM) {         // the lines are almost parallel
	        sc = 0.0f;
	        tc = (b>c ? d/b : e/c);   // use the largest denominator
	    }
	    else {
	        sc = (b*e - c*d) / D;
	        tc = (a*e - b*d) / D;
	    }

	    // get the difference of the two closest points
	    //Vector   dP = w + (sc * u) - (tc * v);  // = L1(sc) - L2(tc)
	    u.multLocal(sc);
	    v.multLocal(tc);
	    temp.set(w);
	    temp.addLocal(u);
	    temp.subtractLocal(v);

	    return temp.lengthSquared();   // return the closest distance
	}
	
	
	/**
	 * Calculate closest approach of two line segments (at ome point in time)
	 * @param s1
	 * 		First segment - segment is from getOrigin() to getOrigin() + getDirection()
	 * @param s2
	 * 		Second segment - segment is from getOrigin() to getOrigin() + getDirection()
	 * @return
	 * 		The shortest distance between any pair of points on the two segments
	 */
	public static float closestApproachSegmentToSegment(Line s1, Line s2) {
		return FastMath.sqrt(closestApproachSegmentToSegmentSquared(s1, s2));
	}

	/**
	 * Calculate closest approach (squared) of two line segments (at ome point in time)
	 * @param s1
	 * 		First segment - segment is from getOrigin() to getOrigin() + getDirection()
	 * @param s2
	 * 		Second segment - segment is from getOrigin() to getOrigin() + getDirection()
	 * @return
	 * 		The shortest distance between any pair of points on the two segments
	 */
	public static float closestApproachSegmentToSegmentSquared(Line s1, Line s2) {
		
		//Vectors along the two segments
		u.set(s1.getDirection());
		v.set(s2.getDirection());
		
		//Vector from origin of second segment to origin of first
		w.set(s1.getOrigin());
		w.subtractLocal(s2.getOrigin());

	    float    a = u.dot(u);        // always >= 0
	    float    b = u.dot(v);
	    float    c = v.dot(v);        // always >= 0
	    float    d = u.dot(w);
	    float    e = v.dot(w);
	    float    D = a*c - b*b;       // always >= 0
	    float    sc, sN, sD = D;      // sc = sN / sD, default sD = D >= 0
	    float    tc, tN, tD = D;      // tc = tN / tD, default tD = D >= 0

	    // compute the line parameters of the two closest points
	    if (D < SMALL_NUM) { // the lines are almost parallel
	        sN = 0.0f;        // force using point P0 on segment S1
	        sD = 1.0f;        // to prevent possible division by 0.0 later
	        tN = e;
	        tD = c;
	    }
	    else {                // get the closest points on the infinite lines
	        sN = (b*e - c*d);
	        tN = (a*e - b*d);
	        if (sN < 0.0) {       // sc < 0 => the s=0 edge is visible
	            sN = 0.0f;
	            tN = e;
	            tD = c;
	        }
	        else if (sN > sD) {  // sc > 1 => the s=1 edge is visible
	            sN = sD;
	            tN = e + b;
	            tD = c;
	        }
	    }

	    if (tN < 0.0) {           // tc < 0 => the t=0 edge is visible
	        tN = 0.0f;
	        // recompute sc for this edge
	        if (-d < 0.0)
	            sN = 0.0f;
	        else if (-d > a)
	            sN = sD;
	        else {
	            sN = -d;
	            sD = a;
	        }
	    }
	    else if (tN > tD) {      // tc > 1 => the t=1 edge is visible
	        tN = tD;
	        // recompute sc for this edge
	        if ((-d + b) < 0.0)
	            sN = 0;
	        else if ((-d + b) > a)
	            sN = sD;
	        else {
	            sN = (-d + b);
	            sD = a;
	        }
	    }
	    // finally do the division to get sc and tc
	    sc = (FastMath.abs(sN) < SMALL_NUM ? 0.0f : sN / sD);
	    tc = (FastMath.abs(tN) < SMALL_NUM ? 0.0f : tN / tD);

	    // get the difference of the two closest points
	    //Vector   dP = w + (sc * u) - (tc * v);  // = S1(sc) - S2(tc)
	    u.multLocal(sc);
	    v.multLocal(tc);
	    temp.set(w);
	    temp.addLocal(u);
	    temp.subtractLocal(v);

	    return temp.lengthSquared();   // return the closest distance
	}
	
	
	/**
	 * Return the time at which two points have their closest approach
	 * @param track1
	 * 		A Line with origin at the initial (time 0) position of the first point,
	 * 		and direction as the velocity of the first point.
	 * @param track2
	 * 		A Line with origin at the initial (time 0) position of the second point,
	 * 		and direction as the velocity of the second point.
	 * @return
	 * 		The time at which the points are closest, may be a time in the past 
	 */
	public static float timeOfTrackClosestApproach( Line track1, Line track2 ) {
		
		//Relative velocity
		//Vector   dv = Tr1.v - Tr2.v;
	    dv.set(track1.getDirection());
	    dv.subtractLocal(track2.getDirection());

	    float dv2 = dv.dot(dv);
	    if (dv2 < SMALL_NUM)      // the tracks are almost parallel
	        return 0.0f;            // any time is ok.  Use time 0.

	    //Vector   w0 = Tr1.P0 - Tr2.P0;
	    w0.set(track1.getOrigin());
	    w0.subtractLocal(track2.getOrigin());
	    
	    //float    cpatime = -dot(w0,dv) / dv2;
	    float cpatime = -w0.dot(dv) / dv2;

	    return cpatime;            // time of CPA
	}

	/**
	 * Return the distance between two points at closest approach
	 * @param track1
	 * 		A Line with origin at the initial (time 0) position of the first point,
	 * 		and direction as the velocity of the first point.
	 * @param track2
	 * 		A Line with origin at the initial (time 0) position of the second point,
	 * 		and direction as the velocity of the second point.
	 * @return
	 * 		The closest distance the points ever reach, may be for a time in the past 
	 * 		(remember to check for this)
	 */
	public static float distanceOfTrackClosestApproach( Line track1, Line track2 ) {
		return FastMath.sqrt(distanceOfTrackClosestApproachSquared(track1, track2));
	}

	
	/**
	 * Return the distance (squared) between two points at closest approach
	 * @param track1
	 * 		A Line with origin at the initial (time 0) position of the first point,
	 * 		and direction as the velocity of the first point.
	 * @param track2
	 * 		A Line with origin at the initial (time 0) position of the second point,
	 * 		and direction as the velocity of the second point.
	 * @return
	 * 		The closest distance the points ever reach, may be for a time in the past 
	 * 		(remember to check for this)
	 */
	public static float distanceOfTrackClosestApproachSquared( Line track1, Line track2 )
	{
	    float time = timeOfTrackClosestApproach( track1, track2);

	    return distanceBetweenTracksSquared(track1, track2, time);

	}

	/**
	 * Return the distance (squared) between two points at a given time
	 * @param track1
	 * 		A Line with origin at the initial (time 0) position of the first point,
	 * 		and direction as the velocity of the first point.
	 * @param track2
	 * 		A Line with origin at the initial (time 0) position of the second point,
	 * 		and direction as the velocity of the second point.
	 * @param time
	 * 		The time at which distance is needed (may be negative)
	 * @return
	 * 		The distance between the points at given time
	 */
	public static float distanceBetweenTracksSquared( Line track1, Line track2, float time)
	{
	    //Point    P1 = Tr1.P0 + (ctime * Tr1.v);
	    //Point along track 1 at time
	    u.set(track1.getDirection());
	    u.multLocal(time);
	    u.addLocal(track1.getOrigin());

	    //Point    P2 = Tr2.P0 + (ctime * Tr2.v);
	    //Point along track 2 at time	    
	    v.set(track2.getDirection());
	    v.multLocal(time);
	    v.addLocal(track2.getOrigin());
	    
	    //return d(P1,P2);           // distance at CPA	    
	    return u.distanceSquared(v);
	}

	/**
	 * Calculate the position of a track at a given time
	 * @param track
	 * 		The track to extrapolate
	 * @param time
	 * 		The time to extrapolate, may be negative
	 * @param store
	 * 		The vector to store result in - if null, a new vector is created
	 * @return
	 * 		The extrapolated position of track at given time
	 */
	public static Vector3f trackAtTime(Line track, float time, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		
	    //Point along track at time
	    store.set(track.getDirection());
	    store.multLocal(time);
	    store.addLocal(track.getOrigin());
	    
	    return store;
	}
	
	/**
	 * Return the distance between two points at a given time
	 * @param track1
	 * 		A Line with origin at the initial (time 0) position of the first point,
	 * 		and direction as the velocity of the first point.
	 * @param track2
	 * 		A Line with origin at the initial (time 0) position of the second point,
	 * 		and direction as the velocity of the second point.
	 * @param time
	 * 		The time at which distance is needed (may be negative)
	 * @return
	 * 		The distance between the points at given time
	 */
	public static float distanceBetweenTracks( Line track1, Line track2, float time) {
		return FastMath.sqrt(distanceBetweenTracksSquared(track1, track2, time));
	}

	
}
