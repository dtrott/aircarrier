package net.java.dev.aircarrier.cards.util;

import java.util.Comparator;

public class ReverseComparator<T extends Comparable<? super T>> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		return -o1.compareTo(o2);
	}

}
