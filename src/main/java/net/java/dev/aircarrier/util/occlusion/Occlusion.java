/**
 * 
 */
package net.java.dev.aircarrier.util.occlusion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


public class Occlusion implements Comparable<Occlusion> {
	public boolean[] c;
	int hashCode;
	String toString;
	
	public Occlusion(int bitmask) {
		this(convertBitmask(bitmask));
	}

	/**
	 * Convert a bitmask int into bits
	 * @param bitmask
	 * 		The bitmask, the least significant 8 bits of this will be used as the 
	 * 		entries of the c array. A 1 bit represents a cube present, 0 represents
	 * 		no cube present
	 * @return
	 * 		The c array for the bitmask
	 */
	private static boolean[] convertBitmask(int bitmask) {
		if (bitmask < 0 || bitmask >= 256) {
			throw new IllegalArgumentException("Only indices 0 to 255 are valid occlusion masks (8 neighbours, each present or absent)");
		}

		boolean[] bits =  new boolean[8]; // 8 bits from the int
		for (int i= 0; i < 8; i++) {
			bits[i]= (bitmask&(1<<i)) != 0;
		}
		
		return bits;
	}
	
	/**
	 * Create an Occlusion
	 * @param c
	 * 		An array which is true in positions where a cube is
	 * occluding light to the cube face in question, false elsewhere.
	 * The array is indexed looking down onto the cube face, with the
	 * cube face aligned by some convention. Index 0 then represents the
	 * cube to the "east" of the cube face in question, 1 is to the "north east",
	 * and so on counter clockwise around the 8 cardinal and diagonal directions.
	 */
	public Occlusion(boolean[] c) {
		this.c = c;
		hashCode = 0;
		for (int i = 0; i < 8; i++) {
			hashCode *= 2;
			hashCode += c[i] ? 1 : 0;
		}
		toString =  
			s(3) + s(2) + s(1) + "\n" +
			s(4) + "X"  + s(0) + "\n" +
			s(5) + s(6) + s(7) + "\n";

	}
	
	/**
	 * @return
	 * 		An array which is true in positions where a cube is
	 * occluding light to the cube face in question, false elsewhere.
	 * The array is indexed looking down onto the cube face, with the
	 * cube face aligned by some convention. Index 0 then represents the
	 * cube to the "east" of the cube face in question, 1 is to the "north east",
	 * and so on counter clockwise around the 8 cardinal and diagonal directions.
	 */
	public boolean[] getC() {
		return c;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Occlusion) {
			Occlusion other = (Occlusion) obj;
			boolean match = true;
			for (int i = 0; i < 8; i++) {
				if (c[i] != other.getC()[i]) {
					match = false;
				}
			}
			return match;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return toString;
	}

	private String s(int index) {
		return c[index] ? "#" : "-";
	}
	
	public String toLine() {
		StringBuilder s = new StringBuilder();
		s.append(c[0] ? "1" : "0");
		for (int i = 1; i < 8; i++) {
			s.append(c[i] ? " 1" : " 0");
		}
		return s.toString();
	}
	
	/**
	 * Return a new Occlusion that is this Occlusion after the
	 * specified Transform
	 * @param t
	 * 		Transform to apply
	 * @return
	 * 		New Occlusion with the transform applied
	 */
	public Occlusion transform(Transform t) {
		boolean[] newC = new boolean[8];
		if (!t.getFlip()) {
			for (int i = 0; i < 8; i++) {
				//Rotate by shifting the cubes two spaces forward in the array,
				//wrapping around
				int targetIndex = (i + t.getRotate()*2)%8;
				if (targetIndex < 0) targetIndex += 8;
				newC[targetIndex] = c[i];
			}				
		} else {
			for (int i = 0; i < 8; i++) {
				//Logic is as for unflipped rotation, but we flip BEFORE the rotation.
				//Hence when we look up in the source, we index backwards, wrapping
				//around. This means that the flip is in the Y axis (Assuming indexing
				//CCW from east, as standard in maths)
				int targetIndex = (i + t.getRotate()*2)%8;
				if (targetIndex < 0) targetIndex += 8;
				int sourceIndex = (-i)%8;
				if (sourceIndex < 0) sourceIndex += 8;
				newC[targetIndex] = c[sourceIndex];
			}
		}
		return new Occlusion(newC);
	}

	public int compareTo(Occlusion o) {
		//Compare by contents, in indexed order, "true" considered greater.
		for (int i = 0; i < 8; i++) {
			boolean a = c[i];
			boolean b = o.getC()[i];
			
			if (a && !b) {
				return 1;
			} else if (b && !a) {
				return -1;
			}
		}
		return 0;
	}
	
	/**
	 * A set of occlusions where:
	 * 		No occlusion x in the set can be transformed onto any
	 * other occlusion y in the set
	 * 		For any occlusion x, there is an occlusion y in the set
	 * that can be transformed onto x. (Obviously y can also be transformed
	 * onto x)
	 * 
	 * Occlusions are sorted by natural ordering of {@link Occlusion}
	 * 
	 * Although this is not necessary, the occlusions are built by searching
	 * through occlusions from lowest bitmask number to highest, so the set
	 * is repeatable even though it is generated each time this class is
	 * initialised.
	 * 
	 */
	public static SortedSet<Occlusion> uniqueOcclusions;
	static {
		uniqueOcclusions = new TreeSet<Occlusion>();
		Transform[] allTransforms = Transform.allTransforms();
		
		for (int i = 0; i < 256; i++) {
			Occlusion o = new Occlusion(i);
			
			//Check that the set does not already include any of the
			//transformed versions of this occlusion
			boolean alreadyDone = false;
			for (Transform t : allTransforms) {
				Occlusion tO = o.transform(t);
				if (uniqueOcclusions.contains(tO)) alreadyDone = true;
			}
			
			//If the occlusion is not already covered (in some transformed
			//state at least) then add it
			if (!alreadyDone) uniqueOcclusions.add(o);
		}
		uniqueOcclusions = Collections.unmodifiableSortedSet(uniqueOcclusions);
	}
	
	/**
	 * The same contents as uniqueOcclusions, but as a list
	 */
	public final static List<Occlusion> uniqueOcclusionList = Collections.unmodifiableList(new ArrayList<Occlusion>(uniqueOcclusions));

	/**
	 * A mapping from each occlusion to the index within the unique occlusion
	 * list of the unique occlusion it can be made from by a transformation.
	 * The mapping actually maps to an IndexAndTransform, where the index is
	 * as described, and the transform is that required to make the unique
	 * occlusion into the key occlusion
	 */
	public static Map<Occlusion, IndexAndTransform> occlusionToTransformedUnique;
	static {
		
		occlusionToTransformedUnique = new HashMap<Occlusion, IndexAndTransform>();
		
		Transform[] allTransforms = Transform.allTransforms();
	
		for (int i = 0; i < 256; i++) {
			Occlusion o = new Occlusion(i);

			//Find the transform we need to get to a unique occlusion
			for (Transform t : allTransforms) {
				Occlusion tO = o.transform(t);
				if (uniqueOcclusions.contains(tO)) {
					int occlusionIndex = uniqueOcclusionList.indexOf(tO);
					
					//Add mapping - we need the index of the occlusion we can transform to,
					//and the INVERSE of the transform we used, since we want to go from
					//the unique occlusion to the current one (o)
					occlusionToTransformedUnique.put(o, new IndexAndTransform(occlusionIndex, t.getInverse()));
				}
			}
		}

		occlusionToTransformedUnique = Collections.unmodifiableMap(occlusionToTransformedUnique);
	}
	
	public final static void main(String[] args) {
		int failures = 0;
		for (int i = 0; i < 256; i++) {
			if (!tryOcclusion(i)) {
				failures++;
			}
		}
		
		System.out.println(failures + " failures.");
	}
	
	private static boolean tryOcclusion(int i) {
		Occlusion o = new Occlusion(i);
		
		IndexAndTransform iat = occlusionToTransformedUnique.get(o);
		
		System.out.println("We need:");
		System.out.println(o);

		System.out.println("So we take unique occlusion number " + iat.getIndex() + ":");
		Occlusion uo = uniqueOcclusionList.get(iat.getIndex());
		System.out.println(uo);
		
		System.out.println("And apply transformation " + iat.getTransform());
		
		System.out.println("Which gives:");
		System.out.println(uo.transform(iat.getTransform()));
		
		boolean equal = (o.equals(uo.transform(iat.getTransform())));
		System.out.println("Which is equal? " + equal);		
		
		return equal;
	}
}