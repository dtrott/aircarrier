package net.java.dev.aircarrier.pqsolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

public class Solver {

	Grid initialGrid;
	
	List<Stage> currentStages;
	
	List<Stage> currentSolutions;
	
	List<Swap> possibleSwaps;
	
	public Solver(Grid initialGrid) {
		super();
		this.initialGrid = initialGrid;

		currentStages = new ArrayList<Stage>();
		currentSolutions = new ArrayList<Stage>();		

		//Make the initial stage - no swap associated, no previous stage,
		//initial grid 
		Stage initialStage = new Stage(null, null, initialGrid);
		currentStages.add(initialStage);
		
		//Make all possible swaps on the grid
		possibleSwaps = new ArrayList<Swap>((initialGrid.getWidth()-1) * (initialGrid.getHeight()-1) * 2);
		for (int x = 0; x < initialGrid.getWidth() - 1; x++) {
			for (int y = 0; y < initialGrid.getHeight() - 1; y++) {
				possibleSwaps.add(new Swap(x, y, x+1, y));
				possibleSwaps.add(new Swap(x, y, x, y+1));
			}			
		}
	}

	public void iterate() {

		//This will be our new set of stages
		List<Stage> newStages = new ArrayList<Stage>();
		
		//For each current stage, add all possible child stages to new stages
		for (Stage stage : currentStages) {
			for (Swap swap : possibleSwaps) {
				//Get the grid for the stage, and duplicate
				Grid grid = stage.getGrid().duplicate();
				//See if the swap does anything
				if (grid.swapAndProcess(swap)) {
					//It does, so make a new stage
					newStages.add(new Stage(swap, stage, grid));
				}
			}
		}
		
		//Our new stages become our current stages
		currentStages = newStages;

		//If any of our new stages are solutions, add them to solutions
		for (Stage stage : currentStages) {
			if (stage.getGrid().tileCount()==0) {
				currentSolutions.add(stage);
			}
		}
	}
	
	
	
	public List<Stage> getCurrentSolutions() {
		return currentSolutions;
	}

	public void setCurrentSolutions(List<Stage> currentSolutions) {
		this.currentSolutions = currentSolutions;
	}

	public List<Stage> getCurrentStages() {
		return currentStages;
	}

	public void setCurrentStages(List<Stage> currentStages) {
		this.currentStages = currentStages;
	}

	static int solveSteps = 0;
	
	public static boolean solve(Grid g, List<Swap> possibleSwaps) {
		solveSteps++;
		if (solveSteps % 100 == 0) System.out.println(solveSteps);
		//System.out.println("Solving");
		for (Swap swap : possibleSwaps) {
			Grid newG = g.duplicate();
			if (newG.swapAndProcess(swap)) {
				//System.out.println("Found a move that does something");
				int tileCount = newG.tileCount();
				//System.out.println(tileCount);
				if (tileCount==0) {
					System.out.println("Solved!");
					System.out.println(swap);
					return true;
				}
				if (solve(newG, possibleSwaps)) {
					System.out.println(swap);
					return true;					
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose a grid file");
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				System.out.println("loading");
				Grid g = Grid.loadFromText(chooser.getSelectedFile());
				
				List<Swap> possibleSwaps = new ArrayList<Swap>((g.getWidth()-1) * (g.getHeight()-1) * 2);
				for (int x = 0; x < g.getWidth() - 1; x++) {
					for (int y = 0; y < g.getHeight() - 1; y++) {
						possibleSwaps.add(new Swap(x, y, x+1, y));
						possibleSwaps.add(new Swap(x, y, x, y+1));
					}			
				}

				solve(g, possibleSwaps);
				
				System.out.println("Finished!");
				
				/*
				Solver s = new Solver(g);
				System.out.println(
						"Initial " + 
						", current stages " + s.getCurrentStages().size() + 
						", current solutions " + s.getCurrentSolutions().size());
				
				for (int i = 0; i < 10; i++) {
					s.iterate();
					System.out.println(
							"Iteration " + i + 
							", current stages " + s.getCurrentStages().size() + 
							", current solutions " + s.getCurrentSolutions().size());
				}
				*/
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
