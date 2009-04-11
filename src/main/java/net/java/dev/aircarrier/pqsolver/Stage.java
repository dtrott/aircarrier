package net.java.dev.aircarrier.pqsolver;

import java.util.ArrayList;
import java.util.List;

public class Stage {

	Swap swap;
	Stage previous;
	Grid grid;
	
	/**
	 * Make a solution stage
	 * @param swap
	 * 		The swap to give the current stage
	 * @param previous
	 * 		The previous stage in the solution, or null if this
	 * 		is the first stage
	 * @param grid
	 * 		The current state of the grid (AFTER the swap in this stage)
	 */
	public Stage(Swap swap, Stage previous, Grid grid) {
		super();
		this.swap = swap;
		this.previous = previous;
		this.grid = grid;
	}

	/**
	 * @return
	 * 		The previous stage in the solution, or null if this
	 * 		is the first stage
	 */
	public Stage getPrevious() {
		return previous;
	}

	/**
	 * @return
	 * 		The swap for the current stage
	 */
	public Swap getSwap() {
		return swap;
	}
	
	
	/**	
	 * @return
	 * 		The current state of the grid (AFTER the swap in this stage)
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * Convert the solution to a list of swaps, in the order they 
	 * should be performed.
	 * @return
	 * 		A list of swaps representing all stages up to this one
	 */
	public List<Swap> listify() {
		List<Swap> list = new ArrayList<Swap>();
		
		//Start from current stage
		Stage current = this;
		
		//While we have a current stage, add it and move to the previous.
		//The first stage has previous as null
		while (current != null) {
			list.add(0, current.getSwap());
			current = current.getPrevious();
		}
		
		return list;
	}
	
}
