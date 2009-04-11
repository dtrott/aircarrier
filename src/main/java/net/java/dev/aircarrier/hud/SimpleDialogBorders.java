package net.java.dev.aircarrier.hud;

public class SimpleDialogBorders implements DialogBorders {

	float[] borders;
	
	/**
	 * Create borders with width 0 all round
	 */
	public SimpleDialogBorders() {
		this(new float[]{0, 0, 0, 0});
	}

	/**
	 * Create borders with same sizes as given borders to copy
	 */
	public SimpleDialogBorders(DialogBorders toCopy) {
		this();
		for (int i = 0; i < 4; i++) borders[i] = toCopy.getBorders()[i];
	}

	
	/**
	 * Create borders with same width on all sides
	 * @param width
	 * 		The width on all sides
	 */
	public SimpleDialogBorders(float width) {
		this(new float[]{width, width, width, width});
	}

	/**
	 * Create borders with all widths set
	 * @param borders
	 * 		Border width array, must have length 4, indexed by BOTTOM, TOP, etc.
	 */
	public SimpleDialogBorders(float[] borders) {
		if (borders.length != 4) throw new IllegalArgumentException("Borders array must have length 4");
		this.borders = borders;
	}
	
	public float[] getBorders() {
		return borders;
	}

	public float getBottom() {
		return borders[BOTTOM];
	}

	public float getLeft() {
		return borders[LEFT];
	}

	public float getRight() {
		return borders[RIGHT];
	}

	public float getTop() {
		return borders[TOP];
	}	

	/**
	 * Set all borders
	 * @param borders
	 * 		Border width array, must have length 4, indexed by BOTTOM, TOP, etc.
	 */
	public void setBorders(float[] borders) {
		if (borders.length != 4) throw new IllegalArgumentException("Borders array must have length 4");
		this.borders = borders;
	}
	
	/**
	 * Set borders with same width on all sides
	 * @param width
	 * 		The width on all sides
	 */
	public void setBorderWidths(float width) {
		setBorders(new float[]{width, width, width, width});		
	}

	public void setBottom(float value) {
		borders[BOTTOM] = value;
	}

	public void setLeft(float value) {
		borders[LEFT] = value;
	}

	public void setRight(float value) {
		borders[RIGHT] = value;
	}

	public void setTop(float value) {
		borders[TOP] = value;
	}	

}
