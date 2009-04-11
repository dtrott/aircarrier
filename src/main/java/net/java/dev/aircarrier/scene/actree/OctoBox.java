package net.java.dev.aircarrier.scene.actree;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class OctoBox {

	/**
	 * The half-sizes of the cube in each axis
	 */
	private Vector3f half = new Vector3f();	
	
	/**
	 * The position of the box center in octree space - octree
	 * cubes are assumed to cover exact units to simplify calculations
	 */
	private Vector3f position = new Vector3f();

	private Vector3f leading = new Vector3f();
	
	/**
	 * The next unit coords to be passed in each direction,
	 * above and below the octobox.
	 * 
	 * The first index is 0 for the bound below, 1 for above
	 * The second index is the axis
	 * 
	 * So for example, if bounds[0][0] is 5, that means that
	 * when moving in the negative x direction, the next plane
	 * to be passed through by the box boundary is the x=5 plane.
	 * No part of the box has moved to an x value strictly
	 * less than 5. This means that if we are moving in the 
	 * negative x direction, we look for collisions when the
	 * leading point moves through x=5. It also means that if
	 * we have zero x velocity, there is no need to check for
	 * collisions with any faces of cubes having x position 4 (since
	 * these are the cubes on the x-negative side of x=5)
	 * 
	 * Similarly, if bounds[1][1] is 0, this means that the
	 * octobox is entirely regions with negative or zero
	 * y value, etc.  
	 * 
	 * When movement causes a check for collision with a unit plane,
	 * this means that the box is considered to have moved at least
	 * some tiny distance past that unit plane, and so the
	 * bounds must be incremented (or decremented) on this axis - this
	 * means that the unit plane will not be checked again for movement
	 * on this axis, and also that it is not possible to hit cubes 
	 * past this plane.
	 * 
	 */
	private int[][] bounds = new int[2][3];

	/**
	 * Indicates whether the octobox is touching the cubegrid on each
	 * side of its bounds
	 * 
	 * The first index is 0 for the bound/face in negative axis direction, 1 for above
	 * The second index is the axis
	 * 
	 * So for example if bounds[0][2] is true, the box is "standing on" something below
	 * 
	 * This is very closely related to "aligned" - a face can only be touching
	 * if it is aligned, but may be aligned without touching - it depends whether
	 * there are actually any cubes on the far side of the aligned plane, within
	 * the integer bounds of the octobox
	 */
	private boolean[][] touching = new boolean[2][3];

	/**
	 * Indicates whether the octobox is EXACTLY with aligned the cubegrid on each
	 * side of its bounds
	 * 
	 * The first index is 0 for the bound/face in negative axis direction, 1 for above
	 * The second index is the axis
	 * 
	 * So for example if bounds[0][2] is true, the box has its bottom face EXACTLY
	 * aligned with a dividing plane of the cube grid
	 * 
	 * This is very closely related to "touching" - a face can only be touching
	 * if it is aligned, but may be aligned without touching - it depends whether
	 * there are actually any cubes on the far side of the aligned plane, within
	 * the integer bounds of the octobox
	 * 
	 */
	private boolean[][] aligned = new boolean[2][3];

	private Vector3i minSearch = new BVector3i();
	private Vector3i maxSearch = new BVector3i();
	private Vector3i search = new BVector3i();
	private Vector3f v = new Vector3f();
	private Vector3i nextBoundaries = new BVector3i();
	
	private NextCollisionReceiver r = new NextCollisionReceiver();
	private Vector3f tempV = new Vector3f();

	private CubeGrid grid;
	
	/**
	 * Heading - this is a temp vector that gives the direction of
	 * motion in each axis - 1 for increasing, -1 for decreasing,
	 * 0 if velocity is exactly 0
	 */
	private Vector3i heading = new BVector3i();
	
	public OctoBox(CubeGrid grid, Vector3f half, Vector3f position) {
		this.grid = grid;
		this.half.set(half);
		this.position.set(position);
		
		//Find initial integer bounds - these non-strictly
		//contain the box - that is, in each axis all points within the
		//box are >= bounds[0][axis] and <= bounds[1][axis]
		for (int axis = 0; axis < 3; axis++) {
			float pos = position.get(axis);
			float h = half.get(axis);
			bounds[0][axis] = (int)FastMath.floor(pos - h);
			bounds[1][axis] = (int)FastMath.ceil(pos + h);
			
			//No alignment or touching!
			touching[0][axis] = false;
			touching[1][axis] = false;
			aligned[0][axis] = false;
			aligned[1][axis] = false;
		}		
	}

	/**
	 * Get the box center position. Please note that if you change
	 * this directly, collisions will not occur - use {@link #slide(Vector3f, float, CubeGrid, CollisionReceiver)}
	 * to slide the box through a {@link CubeGrid} with collisions
	 * @return
	 * 		The {@link OctoBox} center position
	 */
	public Vector3f getPosition() {
		return position;
	}

	//Call only when heading is correct
	private void moveAndUpdate(Vector3f velocity, float time) {
		//If time is zero, do nothing
		if (time == 0) return;
		
		//Move to our position at given time
		for (int j = 0; j < 3; j++){
			position.set(j, position.get(j) + time * v.get(j));
		}
		//Update:
		//	1) trailing bounds and 
		//	2) Changes to touching/aligned due to movement normal to the touching/aligned faces
		for (int j = 0; j < 3; j++){
			float pos = position.get(j);
			float h = half.get(j);
			
			//If we have actual movement on this axis, we need to update bounds, alignment and touching
			if (heading.get(j) != 0) {

				//Update the trailing bounds - the leading bounds are updated
				//elsewhere
				if (heading.get(j) == 1) {
					bounds[0][j] = (int)FastMath.floor(pos - h);				
				} else if (heading.get(j) == -1) {
					bounds[1][j] = (int)FastMath.ceil(pos + h);								
				}

				//If we make any movement along an axis, the faces perpendicular to that
				//axis will no longer be aligned or touching
				//Note that if we are moving into a new aligned/touching position this will be
				//detected elsewhere
				//Moving in either direction breaks touching - we are only touching
				//if we are just exactly aligned in contact, NOT penetrating, at least
				//according to the integer bounds etc.
				touching[0][j] = false;
				touching[1][j] = false;
				aligned[0][j] = false;
				aligned[1][j] = false;
			}
		}
		
		//Now finalise changes to touching/aligned due to movement
		//in the plane of the touching/aligned faces
		//On all axes that are aligned, update whether we are touching
		for (int j = 0; j < 3; j++){
			for (int direction = 0; direction < 1; direction++) {
				if (aligned[direction][j]) {
					//Check if there are any cubes within the bounds, on the other side of the
					//aligned octobox face - if so, we are touching.
					//We know that we have set aligned false for each axis 
					//if we have any movement on that axis, so here we are checking
					//what happens as we slide exactly along a plane, possibly in and
					//out of contact with boxes on the other side of it
					
					//Work out the cube grid coord for cubes on the far side of the
					//aligned face from the octobox
					int boundary = bounds[direction][j];
					if (direction==0) boundary--;
					
					//Now scan on that coord within our bounds - iff there are any cubes
					//we are touching
					touching[direction][j] = scanInPlane(j, boundary);
				}
			}
		}		
		
	}
	
	/**
	 * Scan for any cubes in the specified plane bordering the octobox, within
	 * the bounds of the octobox
	 * @param grid
	 * 		The grid to scan
	 * @param axis
	 * 		The normal axis of the plane
	 * @param position
	 * 		The coordinate of the plane
	 * @return
	 * 		True iff there is a cube
	 */
	private boolean scanInPlane(int axis, int position) {
		//Check for collision of the leading box plane with the cube grid contents
		//on the far side of the unit plane we are passing through
		for (int i = 0; i < 3; i++) {
			if (i == axis) {
				//In the axis we are checking, we just search a single
				//value
				minSearch.set(i, position);
				maxSearch.set(i, position);
			} else {
				//In the other axes, we indices given by current integer bounds
				minSearch.set(i, bounds[0][i]);
				//Note conversion from boundaries to cube indices - the 
				//nth cube goes up to the (n+1)th boundary, so we subtract one
				maxSearch.set(i, bounds[1][i] - 1);	
			}
		}
		
		//Search for cubes
		boolean collided = false;
		for (int x = minSearch.getX(); x <= maxSearch.getX(); x++) {
			for (int y = minSearch.getY(); y <= maxSearch.getY(); y++) {
				for (int z = minSearch.getZ(); z <= maxSearch.getZ(); z++) {
					search.set(x, y, z);
					if (grid.getPresence(search)) {
						collided = true;
					}
				}
			}
		}
		
		return collided;
	}
	
	public void slide(Vector3f velocity, float maxTime, CollisionReceiver receiver) {
		v.set(velocity);
		
		float elapsedTime = 0;
		
		updateHeading(v);

		//Find extreme point - this is the point that will break through a
		//unit plane first, in each dimension - essentially it is the
		//"leading" corner of the box where there is motion in all axes.
		//Where there is no motion in an axis, the position in that axis
		//doesn't matter, so will be the box center position.
		updateLeading();
		
		//Keep scanning for more collisions until we leave the CubeGrid,
		//run out of time, or are told to stop scanning by the receiver
		for (int repeats = 0; repeats < 100000; repeats++) {

			//If velocity is zero, stop
			if (isZero(v)) return;

			//Find the integer positions of the next unit planes to be reached
			//by the extreme point - these are selected from the current 
			//integer bounds, according to heading
			for (int i = 0; i < 3; i++) {
				int h = heading.get(i);
				if (h == 1) {
					nextBoundaries.set(i, bounds[1][i]); 
				} else {
					//Note that if h == 0 we don't really mind which plane we pick
					nextBoundaries.set(i, bounds[0][i]); 				
				}
			}
			
			//Now we find which unit plane will be passed through first, 
			//by the leading point, since we only get collisions when 
			//this happens (we don't necessarily always get a collision though!)
			float minTime = Float.MAX_VALUE;
			int minAxis = 0;
			for (int i = 0; i < 3; i++) {
				float vi = v.get(i);
				if (vi != 0) {
					float t = (nextBoundaries.get(i) - leading.get(i))/vi;
					if (t < minTime) {
						minTime = t;
						minAxis = i;
					}
				}
			}
			
			//If we are out of time before next collision
			if (elapsedTime + minTime > maxTime) {
				//Move to our position at maxTime
				moveAndUpdate(v, maxTime - elapsedTime);
				//We're done, no more collisions, and box is at final position
				return;
			}
			
			//Translate to the (possible) collision time 
			//(update position, elapsed time and leading position)
			moveAndUpdate(v, minTime);
			elapsedTime += minTime;
			updateLeading();
	
			int headingMinAxis = heading.get(minAxis);
			
			//work out the coordinate in the cube grid for the plane
			//of cubes on the far side of the unit plane we are passing through
			//This is the same as the boundary plane position if going forwards,
			//or one less if going back
			int collisionPlaneCubeIndex = nextBoundaries.get(minAxis);
			if (headingMinAxis < 0) collisionPlaneCubeIndex --;
			
			//Check for collision of the leading box plane with the cube grid contents
			//on the far side of the unit plane we are passing through
			for (int i = 0; i < 3; i++) {
				if (i == minAxis) {
					//In the axis we are checking, we just search a single
					//value
					minSearch.set(i, collisionPlaneCubeIndex);
					maxSearch.set(i, collisionPlaneCubeIndex);
				} else {
					//In the other axes, we indices given by current integer bounds
					minSearch.set(i, bounds[0][i]);
					//Note conversion from boundaries to cube indices - the 
					//nth cube goes up to the (n+1)th boundary, so we subtract one
					maxSearch.set(i, bounds[1][i] - 1);	
				}
			}
			
			//Search for cubes
			boolean collided = false;
			for (int x = minSearch.getX(); x <= maxSearch.getX(); x++) {
				for (int y = minSearch.getY(); y <= maxSearch.getY(); y++) {
					for (int z = minSearch.getZ(); z <= maxSearch.getZ(); z++) {
						search.set(x, y, z);
						if (grid.getPresence(search)) {
							collided = true;
						}
					}
				}
			}
			if (collided) {
				
				//We are now touching and aligned on the collided bounds
				touching[headingMinAxis>0?1:0][minAxis] = true;
				aligned[headingMinAxis>0?1:0][minAxis] = true;
				
				//FIXME send correct collision center
				if (!receiver.acceptCollision(elapsedTime, position, minAxis, position)) {
					return;
				}
				
			//If we had no collision, we have passed JUST through the plane in
			//terms of our integer bounds, so we can no collide with cubes on the
			//other side of the plane, and will not check for collision with the
			//same plane again (unless we go past it the other way first)
			} else {
				//Now we move the next boundary along on the axis we will check
				//for collisions, so we will not check it again
				if (headingMinAxis > 0) {
					bounds[1][minAxis]++;
				} else {
					bounds[0][minAxis]--;					
				}
			}
	
		}
	}

	public int[][] getBounds() {
		return bounds;
	}

	public boolean[][] getTouching() {
		return touching;
	}

	public boolean[][] getAligned() {
		return aligned;
	}

	private void updateLeading() {
		leading.set(position);
		for (int i = 0; i < 3; i++) {
			leading.set(i, leading.get(i) + heading.get(i) * half.get(i));
		}
	}

	private void updateHeading(Vector3f v) {
		for (int i = 0; i < 3; i++) {
			int val = 0;
			double vComp = v.get(i);
			if (vComp > 0) {
				val = 1;
			} else if (vComp < 0) {
				val = -1;
			}
			heading.set(i, val);
		}
	}

	public static boolean isZero(Vector3f v) {
		return (v.getX()==0 && v.getY() == 0 && v.getZ() == 0);
	}	
	
	/**
	 * Slide the box through a grid, making sure we slide along
	 * any cubes we hit in the grid.
	 * @param v
	 * 		The velocity at which to move
	 * @param maxTime
	 * 		The maximum time for which to move
	 * @param grid
	 * 		The grid we are moving through
	 */
	public void slideAlong(Vector3f v, float maxTime) {
		float elapsedTime = 0;
		tempV.set(v);
		
		while ((elapsedTime < maxTime) && (!isZero(tempV))) {			
			r.reset();			
			slide(tempV, maxTime - elapsedTime, r);
			//If we had no collision we are done
			if (!r.collided()) return;
			
			//We had a collision, so advance time and
			//kill velocity in the collision axis
			elapsedTime += r.getElapsedTime();
			tempV.set(r.getCollisionAxis(), 0);
		}
		
	}
	
}
