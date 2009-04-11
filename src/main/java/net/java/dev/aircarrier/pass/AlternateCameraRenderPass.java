/*
 *  $Id: AlternateCameraRenderPass.java,v 1.1 2007/03/07 20:53:13 shingoki Exp $
 *
 * 	Copyright (c) 2005-2006 shingoki
 *
 *  This file is part of AirCarrier, see http://aircarrier.dev.java.net/
 *
 *    AirCarrier is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.

 *    AirCarrier is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.

 *    You should have received a copy of the GNU General Public License
 *    along with AirCarrier; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package net.java.dev.aircarrier.pass;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.Pass;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

/**
 * <code>RenderPass</code> renders the spatials attached to it as normal,
 * including rendering the renderqueue at the end of the pass.
 * 
 * @author Joshua Slack
 * @version $Id: AlternateCameraRenderPass.java,v 1.1 2007/03/07 20:53:13 shingoki Exp $
 */
public class AlternateCameraRenderPass extends Pass {
	private static final long serialVersionUID = 1778264776758869736L;
	
	Camera camera;
	
	Vector3f v = new Vector3f();
	Vector3f l = new Vector3f();
	Vector3f u = new Vector3f();
	Vector3f d = new Vector3f();
	
	public static AlternateCameraRenderPass makeHUDPass() {
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		
        Camera cam = display.getRenderer().createCamera(display.getWidth(),
                display.getHeight());
        cam.setParallelProjection(true);
        float aspect = (float) display.getWidth() / display.getHeight();
        cam.setFrustum(-1000, 1000, -50 * aspect, 50 * aspect, -50, 50);
        
        Vector3f loc = new Vector3f(0.0f, 0.0f, 10.0f);
        Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
        /** Move our camera to a correct place and orientation. */
        cam.setFrame(loc, left, up, dir);
        /** Signal that we've changed our camera's location/frustum. */
        cam.update();
        
        AlternateCameraRenderPass pass = new AlternateCameraRenderPass();
        pass.setCamera(cam);
        
        return pass;
	}
	
    protected void cameraPerspective(Camera cam) {
    	DisplaySystem display = DisplaySystem.getDisplaySystem();
        cam.setFrustumPerspective( 60.0f, (float) display.getWidth()
                / (float) display.getHeight(), 1, 2000 );
        cam.setParallelProjection( false );
        cam.update();
    }

    protected void cameraParallel(Camera cam) {
    	DisplaySystem display = DisplaySystem.getDisplaySystem();
        cam.setParallelProjection( true );
        cam.setFrustum( -100, 1000, display.getWidth()/2, -display.getWidth()/2, -display.getHeight()/2, display.getHeight()/2 );
        cam.update();
    }

	
    public void doRender(Renderer r) {
		/*
    	//Keep the camera previously set
    	Camera savedCamera = DisplaySystem.getDisplaySystem().getRenderer().getCamera();

    	//If we have one, set our camera
		if (camera != null) {
    		DisplaySystem.getDisplaySystem().getRenderer().setCamera(camera);
    		camera.update();
    		camera.apply();
    	}
				
        for (int i = 0, sSize = spatials.size(); i < sSize; i++) {
            Spatial s = spatials.get(i);
            r.draw(s);
        }
        r.renderQueue();
        
        //Set the old camera back
        DisplaySystem.getDisplaySystem().getRenderer().setCamera(savedCamera);
        savedCamera.update();
        savedCamera.apply();
        */
    	
    	Camera c = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
    	v.set(c.getLocation());
    	l.set(c.getLeft());
    	u.set(c.getUp());
    	d.set(c.getDirection());
    	
		c.getLocation().set(0, 0, -1);
		c.getLeft().set(1, 0, 0);
		c.getUp().set(0, 1, 0);
		c.getDirection().set(0, 0, 1);
		
		cameraParallel(c);
		c.update();
		c.apply();
		
        for (int i = 0, sSize = spatials.size(); i < sSize; i++) {
            Spatial s = spatials.get(i);
            r.draw(s);
        }
        r.renderQueue();

		c.getLocation().set(v);
		c.getLeft().set(l);
		c.getUp().set(u);
		c.getDirection().set(d);
		cameraPerspective(c);
		c.update();
		c.apply();
        
    }

    /**
     * @return
     * 		Camera used for pass - may be null, in which case
     * 		camera is not altered for pass, and this is
     * 		equivalent to a RenderPass
     */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * @param camera
     * 		Camera used for pass - may be null, in which case
     * 		camera is not altered for pass, and this is
     * 		equivalent to a RenderPass
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
    
}
