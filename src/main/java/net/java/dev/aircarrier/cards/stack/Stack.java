package net.java.dev.aircarrier.cards.stack;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Stack {

	List<CardPlacement> contents;
	List<CardPlacement> umContents;
	boolean spread;
	String name;
	
	public Stack(String name) {
		this(name, new LinkedList<CardPlacement>());
	}
	
	public Stack(String name, Collection<CardPlacement> cards) {
		this.contents = new LinkedList<CardPlacement>(cards);
		umContents = Collections.unmodifiableList(this.contents);
		this.name = name;
	}
	
	public List<CardPlacement> getContents() {
		return umContents;
	}

	List<CardPlacement> contents() {
		return contents;
	}
	
	public boolean isSpread() {
		return spread;
	}

	public void setSpread(boolean spread) {
		this.spread = spread;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		String s = getName() + " [";
		boolean first = true;
		for (CardPlacement cp : contents) {
			s += (first ? "" : ", ") + cp.toString();
			first = false;
		}
		s += "]";
		return s;
	}
	
}
