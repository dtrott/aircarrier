package net.java.dev.aircarrier.pqsolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;

public class Grid {

	int width;
	int height;
	
	Tile tiles[][];
	
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setTile(x, y, Tile.EMPTY);
			}			
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public void setTile(int x, int y, Tile tile) {
		tiles[x][y] = tile;
	}
	
	public void applySwap(Swap s) {
		applySwap(s.getX1(), s.getY1(), s.getX2(), s.getY2()); 
	}

	public void applySwap(int x1, int y1, int x2, int y2) {
		Tile t1 = getTile(x1, y1);
		setTile(x1, y1, getTile(x2, y2));
		setTile(x2, y2, t1);
	}

	/**
	 * @return
	 * 		A copy of the grid, using a new array
	 */
	public Grid duplicate() {
		Grid g = new Grid(getWidth(), getHeight());
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				g.setTile(x, y, getTile(x, y));
			}			
		}
		return g;
	}
	
	/**
	 * Make a swap, and process the result
	 * @param s
	 * 		The swap to make
	 * @return
	 * 		True if the swap caused any matches in subsequent processing
	 */
	public boolean swapAndProcess(Swap s) {
		applySwap(s);
		return process();
	}
	
	/**
	 * Process a move, matching and settling until nothing
	 * more happens
	 * 
	 * @return
	 * 		True if there was an initial match
	 * 		False if the initial match failed - this is an invalid move
	 * 		
	 */
	public boolean process() {
		
		boolean finished = false;
		
		debug("process");

		//If there isn't an immediate match, then it isn't a valid move, so stop
		if (!match()) return false;
		
		while (!finished) {
			debug("process loop");

			settle();
			debug("settled");
			if (match()) {
				debug("we had matches");
				finished = false;
			} else {
				debug("no matches");
				finished = true;
			}
		}
		
		return true;
	}
	
	
	public void debug(String s) {
		//System.out.println(s);
	}
	
	
	/**
	 * Find all matches and process them
	 * @return
	 * 		True if any change resulted
	 */
	public boolean match() {
		boolean stuffHappened = false;
		
		debug("matching");
		
		boolean inMatch[][] = new boolean[width][height];

		//Clear matching array
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				inMatch[x][y] = false;
			}			
		}
		
		//Scan row first for row matches starting from x, y
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width - 2; x++) {
				Tile t = getTile(x, y);
				if (t.matches(getTile(x+1, y)) && t.matches(getTile(x+2, y))) {
					inMatch[x][y] = true;
					inMatch[x+1][y] = true;
					inMatch[x+2][y] = true;
					debug("Found match at " + x + ", " + y);
				}
			}			
		}

		//Scan column first for column matches starting from x, y
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height-2; y++) {
				Tile t = getTile(x, y);
				if (t.matches(getTile(x, y+1)) && t.matches(getTile(x, y+2))) {
					inMatch[x][y] = true;
					inMatch[x][y+1] = true;
					inMatch[x][y+2] = true;
					debug("Found match at " + x + ", " + y);
				}
			}			
		}

		//All tiles that are part of matches are now known, so clear them.
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (inMatch[x][y]) {
					stuffHappened = true;
					clear(x, y);
				}
			}
		}

		debug("Finished match, stuff happened? " + stuffHappened);

		return stuffHappened;
	}
	
	public int tileCount() {
		int tiles = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (getTile(x, y) != Tile.EMPTY) tiles++;
			}
		}
		return tiles;
	}
	
	/**
	 * Clear a tile. If it is a SKULL5, it will "explode", clearing
	 * tiles in a 3x3 square centered on x, y.
	 * @param x
	 * 		X coord
	 * @param y
	 * 		Y coord
	 */
	public void clear(int x, int y) {
		Tile toClear = getTile(x, y);
		if (toClear == Tile.SKULL5) {
			clearAround(x, y);
		} else {
			setTile(x, y, Tile.EMPTY);
		}
	}
	
	/**
	 * Clear all tiles in a 3x3 square centered on x, y.
	 * If any of these tiles are SKULL5 tiles, then clear around
	 * them also (recursively)
	 * @param x
	 * 		X coord
	 * @param y
	 * 		Y coord
	 */
	public void clearAround(int x, int y) {

		//Clear the tile itself immediately (so that clear doesn't recurse on SKULL5's)
		setTile(x, y, Tile.EMPTY);

		//Clear any tiles around
		for (int xo = -1; xo <= 1; xo++) {
			for (int yo = -1; yo <= 1; yo++) {
				int xc = x + xo;
				int yc = y + yo;
				if ((xc >= 0) && (xc < width) && (yc >= 0) && (yc < height)) {
					clear(xc, yc);
				}
			}						
		}
		
	}
	
	/**
	 * Settle the tiles, according to "gravity" where tiles drop into empty spaces
	 */
	public void settle() {
		debug("starting settle");
		//Keep stepping until we have completely settled
		while (settleStep()) {
			debug("made a settle step");
		}
	}

	/**
	 * Apply one step of a settling process according to "gravity" 
	 * where tiles drop into empty spaces. This moves tiles at most
	 * one step downwards
	 * @return
	 * 		True if any tiles moved
	 */
	public boolean settleStep() {
		
		debug("settle step");
		boolean movement = false;
		
		//We only check up to row beneath the top row - nothing can fall into top row
		for (int y = 0; y < height - 1; y++) {
			for (int x = 0; x < width; x++) {
				//If there is a tile to fall into this position, make it fall
				if (getTile(x, y) == Tile.EMPTY && getTile(x, y+1) != Tile.EMPTY) {
					//Swap with row above, this makes the tiles "fall"
					applySwap(x, y, x, y+1);
					movement = true;
				}
			}			
		}
		//Return whether any movements have been made
		return movement;
	}
	
	public static Grid loadFromText(File f) throws IOException {
		BufferedReader lines = null;
		try {
			lines = new BufferedReader(new FileReader(f));
			
			String line = lines.readLine();
			if (line == null) throw new IOException("Missing grid size line");
			
			int width;
			int height;
			
			try {
				StringTokenizer tokens = new StringTokenizer(line, ",");
				width = Integer.parseInt(tokens.nextToken());
				height = Integer.parseInt(tokens.nextToken());
			} catch (NoSuchElementException nsee) {
				throw new IOException("Missing width or height on first line '" + line + "', should be 'width, height'");
			} catch (NumberFormatException nfe) {
				throw new IOException("Invalid width or height on first line '" + line + "', should be 'width, height'");
			}
			
			Grid g = new Grid(width, height);
			//Note that grid origin is in "bottom left", y coordinate counts upwards,
			//so we need to read lines backwards from file
			for (int y = height - 1; y >= 0; y--) {
				line = lines.readLine();
				if (line == null) throw new IOException("Grid is missing a row");
				if (line.length() < width) throw new IOException("Grid line is too short");
				for (int x = 0; x < width; x++) {
					String s = line.substring(x, x+1);
					Tile t = null;
					for (Tile checkTile : Tile.values()) {
						if (checkTile.getLetter().equals(s)) t = checkTile;
					}
					if (t == null) {
						throw new IOException("Grid character '" + s + "' is not valid");
					} else {
						g.setTile(x, y, t);
					}
				}
			}
			
			return g;
		} finally {
			if (lines != null) {
				lines.close();
			}
		}
	}
		
	public void print() {
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				Tile t = getTile(x, y);
				System.out.print(t.getLetter());
			}
			System.out.println(" " + y);
		}
		System.out.println();
		for (int x = 0; x < width; x++) {
			System.out.print(x);
		}	
		System.out.println();		
	}
	
	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose a grid file");
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				System.out.println("loading");
				Grid g = Grid.loadFromText(chooser.getSelectedFile());
				System.out.println("printing");
				g.print();
				System.out.println("swapping");
				g.swapAndProcess(new Swap(7, 0, 6, 0));
				//g.applySwap(new Swap(1, 4, 2, 4));
				System.out.println("printing again");
				g.print();
				System.out.println("done");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
		
	}
	
}
