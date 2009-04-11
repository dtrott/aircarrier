package net.java.dev.aircarrier.util;

import java.util.List;

import com.jme.math.FastMath;

public class ListUtils {

	/**
	 * Shuffles a list in place to randomise it.
	 * This is done by removing a random element, and
	 * inserting it in a random position, repeated for a number
	 * of times equal to the length of the list
	 * @param <T>
	 * 		The type of the list
	 * @param list
	 * 		The list to shuffle
	 */
	public static <T> void shuffleList(List<T> list) {
		for (int i = 0; i < list.size(); i++) {
			int from = FastMath.rand.nextInt(list.size()-1);
			int to = FastMath.rand.nextInt(list.size()-1);
			T element = list.remove(from);
			list.add(to, element);
		}
	}
	
}
