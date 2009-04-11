/**
 * 
 */
package net.java.dev.aircarrier.util;

import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.Timer;

/**
 * @author shingoki
 *
 */
public class NanoTimer extends Timer {

	public final static long RESOLUTION = 1000000000;
	public final static float RESOLUTION_F = RESOLUTION;
	public final static float DEFAULT_RATE = 60;
	
	public static NanoTimer instance = null;

	long firstTime;
	long lastFrame;
	boolean firstFrame;
	
	float frameRate;
	float frameTime;
	
	private NanoTimer() {
		reset();
	}
	
    /**
     * Returns the high resolution timer. Timer is a singleton class so only one
     * instance of Timer is allowed.
     *
     * @return the timer defined by the SystemProvider
     */
    public static Timer getTimer() {
        if (instance == null) {
            if(DisplaySystem.getSystemProvider() == null) {
                throw new JmeException("Display System must be initialized before Timer.");
            }
            instance = new NanoTimer();
        }
        
        return instance;
    }

	/* (non-Javadoc)
	 * @see com.jme.util.Timer#getFrameRate()
	 */
	@Override
	public float getFrameRate() {
		return frameRate;
	}

	/* (non-Javadoc)
	 * @see com.jme.util.Timer#getResolution()
	 */
	@Override
	public long getResolution() {
		return RESOLUTION;
	}

	/* (non-Javadoc)
	 * @see com.jme.util.Timer#getTime()
	 */
	@Override
	public long getTime() {
		return System.nanoTime() - firstTime;
	}

	/* (non-Javadoc)
	 * @see com.jme.util.Timer#getTimePerFrame()
	 */
	@Override
	public float getTimePerFrame() {
		return frameTime;
	}

	/* (non-Javadoc)
	 * @see com.jme.util.Timer#reset()
	 */
	@Override
	public void reset() {
		firstTime = System.nanoTime();
		lastFrame = firstTime;
		firstFrame = true;
		frameRate = DEFAULT_RATE;
		frameTime = 1f/DEFAULT_RATE;
	}

	/* (non-Javadoc)
	 * @see com.jme.util.Timer#update()
	 */
	@Override
	public void update() {
		long thisFrame = System.nanoTime();
		float elapsed = (float)(thisFrame - lastFrame);

		//Remember time for next call
		lastFrame = thisFrame;
		
		//If this is the first time we have been called, frame rate and time are defaults
		if (firstFrame) {
			firstFrame = false;
			frameRate = DEFAULT_RATE;
			frameTime = 1f/DEFAULT_RATE;
		} else {
			//Calculate frame rate and time from elapsed time
			frameRate = RESOLUTION_F / elapsed;
			frameTime = elapsed / RESOLUTION_F;
		}		
	}

}
