/*
 *  $Id: SimplePhysicsTest.java,v 1.4 2006/07/21 23:59:19 shingoki Exp $
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

package net.java.dev.aircarrier.physics;


//import org.odejava.collision.Contact;
//import org.odejava.ode.Ode;

//import com.jme.app.SimpleGame;
//import com.jme.math.Vector3f;
//import com.jme.scene.shape.Box;
//import com.jmex.physics.DynamicPhysicsObject;
//import com.jmex.physics.PhysicsObject;
//import com.jmex.physics.PhysicsWorld;
//import com.jmex.physics.StaticPhysicsObject;
//import com.jmex.physics.contact.PhysicsCallBack;

public class SimplePhysicsTest 
//extends SimpleGame 
{
//
//	/**
//	 * Inits everything needed for this test.
//	 */
//	protected void simpleInitGame() {
//		display.setTitle("Simple Test");
//
//		// Set up the PhysicsWorld. It's got a couple of default
//		// values, e.g. Earths gravity.
//		PhysicsWorld.create();
//
//		// Here we tell the PhysicsWorld how many times per second we would like
//		// to update it. It'll make the PhysicsWorlds internal timer govern the
//		// frequency of update calls, thus obtaining frame rate independance.
//		// We set it to 100 updates per second - the default is no restriction.
//		PhysicsWorld.getInstance().setUpdateRate(50);
//
//		// Here we tell the PhysicsWorld how much should change with each
//		// update. A bigger value leads to a faster animation. A step size
//		// of 2/UPS (updates/sec) seem to give a rather nice simulation/result.
//		PhysicsWorld.getInstance().setStepSize(2f/50f);
//
//		// Creates the box that makes out the floor (graphics only).
//		Box floorGraphics = new Box("Floor", new Vector3f(), 50, 1, 50);
//
//		// We move it down 5 units, and away from the camera 10 units.
//		floorGraphics.setLocalTranslation(new Vector3f(0, -5, 10));
//
//		// Add the graphical representations to the rootNode. You could also get
//		// references to them by calling PhysicsObject.getSpatial().
//		rootNode.attachChild(floorGraphics);
//
//		// In order to add it to the PhysicsWorld we need to create a
//		// PhysicsObject from it. Note that we don't pass a mass in the constructor.
//		// This is because it's static.
//		PhysicsObject floorPhysics = new StaticPhysicsObject(floorGraphics);
//
//		// Creates the box that falls down on the floor.
//		Box boxGraphics = new Box("Box", new Vector3f(), 1f, 1f, 1f);
//
//		// We move it 10 units up, and 10 units away from the camera.
//		boxGraphics.setLocalTranslation(new Vector3f(-10, -10000, 0));
//
//		// Attach the graphical representation to the scene.
//		rootNode.attachChild(boxGraphics);
//
//		// Create a dynamic physics object from it. Because it is dynamic, we
//		// need to provide it with a mass.
//		DynamicPhysicsObject boxPhysics = new DynamicPhysicsObject(boxGraphics, 4f);
//
//	
//		// We move it 10 units up, and 10 units away from the camera.
//		boxGraphics.setLocalTranslation(new Vector3f(-10, 10, 0));
//
//		
//		// Creates the box that falls down on the floor.
//		Box boxGraphics2 = new Box("Box", new Vector3f(), 1f, 1f, 1f);
//
//		// We move it 10 units up, and 10 units away from the camera.
//		boxGraphics2.setLocalTranslation(new Vector3f(-10, 7.5f, 0.5f));
//
//		// Attach the graphical representation to the scene.
//		rootNode.attachChild(boxGraphics2);
//
//		// Create a dynamic physics object from it. Because it is dynamic, we
//		// need to provide it with a mass.
//		PhysicsObject boxPhysics2 = new DynamicPhysicsObject(boxGraphics2, 3f);
//
//		PhysicsWorld.getInstance().addObject(boxPhysics2);
//
//		
//		// Add the physical representations to the PhysicsWorld.
//		PhysicsWorld.getInstance().addObject(floorPhysics);
//		PhysicsWorld.getInstance().addObject(boxPhysics);
//		
//		PhysicsWorld.getInstance().setPhysicsCallBack(new MyCallback());
//	}
//
//	/**
//	 * Gets called on application ending.
//	 */
//	protected void cleanup() {
//		super.cleanup();
//		// Before ending your application you should always clean up the
//		// PhysicsWorld.
//		PhysicsWorld.getInstance().cleanup();
//	}
//
//	/**
//	 * Gets called every frame.
//	 */
//	protected void simpleUpdate() {
//		// We must call this method in order to make the simulation step
//		// forward. It doesn't matter how many times per second we make the call,
//		// as long as we have a framerate >= the updaterate we've given the
//		// PhysicsWorld. This is because of the PhysicsWorlds internal timer,
//		// that govern the frequency of actual updates.
//		PhysicsWorld.getInstance().update(tpf);
//	}
//
//	/**
//	 * Application entry point.
//	 *
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		SimplePhysicsTest app = new SimplePhysicsTest();
//		app.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG, SimplePhysicsTest.class.getClassLoader()
//				.getResource("jmextest/data/images/jmephysics_logo.png"));
//		app.start();
//	}
//
//	private class MyCallback implements PhysicsCallBack {
//
//		public void onContact(PhysicsObject obj1, PhysicsObject obj2, Contact contact) {
//			contact.setMode(Ode.dContactBounce | Ode.dContactApprox1);
//			contact.setBounce(0.4f);
//			contact.setBounceVel(1f);
//			contact.setMu(0f);
//		}
//
//		public void defaultContact(Contact contact) {
//		}
//		
//	}
//	
}