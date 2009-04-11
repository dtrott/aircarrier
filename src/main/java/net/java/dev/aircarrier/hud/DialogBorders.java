package net.java.dev.aircarrier.hud;

public interface DialogBorders {

    public static int RIGHT     = 0;
    public static int TOP       = 1;
    public static int LEFT      = 2;
    public static int BOTTOM    = 3;

	public float[] getBorders();
	
	public float getTop();
	public float getBottom();
	public float getLeft();
	public float getRight();
	
}
