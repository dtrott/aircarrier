package net.java.dev.aircarrier.hud;

import net.java.dev.aircarrier.acobject.Acobject;

import com.jme.math.Vector3f;
import com.jme.scene.Controller;

/**
 * Control a radar to show pings relative to a center node,
 * on a circular radar area.
 * @author goki
 */
public class CenteredCircularRadarController extends Controller {
	private static final long serialVersionUID = 8720962534725579344L;
	
	RadarNode radar;
	Acobject center;

	Vector3f offset = new Vector3f();
	Vector3f centerTranslation = new Vector3f();
	Vector3f forward = new Vector3f();
	Vector3f right = new Vector3f();
	Vector3f globalUp = new Vector3f(0, 1, 0);
	
	
	float worldToRadarFactor;
	
	boolean rotatedByCenter = true;
	
	/**
	 * Make a controller, which updates a specified radar node
	 * to position the pings on it
	 * @param radar
	 * 		The radar to control
	 * @param center
	 * 		The node giving the world position mapped to radar center
	 * @param worldToRadarFactor
	 * 		The scale factor by which o multiply world vectors to give
	 * 		radar vectors. Note that the radar displays normalised vectors,
	 * 		so this number should be the maximum horizontal distance displayed 
	 * 		on the radar without clipping
	 */
	public CenteredCircularRadarController(RadarNode radar, Acobject center, float worldToRadarFactor) {
		super();
		this.radar = radar;
		this.center = center;
		this.worldToRadarFactor = worldToRadarFactor;
	}

	@Override
	public void update(float time) {
		//Get center of radar
		centerTranslation.set(center.getPosition());
		
		//If rotated by center, calculate center axes
		if (rotatedByCenter) {
			//Get center forward vector
			center.getRotation().getRotationColumn(2, forward);
			
			//Cross with world up axis to get radar right axis
			forward.cross(globalUp, right);
			
			//Cross right and up again to get a horizontal forward
			globalUp.cross(right, forward);
			
			right.normalizeLocal();
			forward.normalizeLocal();
		}
		
		float radius = radar.getRadius();
		
		//Get each ping, and place it according to scaling
		for (PingNode ping : radar.getPings()) {
			offset.set(ping.getTargetPosition());
			offset.subtractLocal(centerTranslation);			

			//If rotated by center, then use center node axes to rotate 
			if (rotatedByCenter) {
				float x = offset.dot(right);
				float y = offset.dot(forward);
				offset.set(x, 0, y);
			}

			//Use X and Z components of offset - usually the "world" uses Y for height, which is not shown on radar
			offset.set(offset.getX(), offset.getZ(), 0);

			transform(offset, radius);
			
			
			ping.getLocalTranslation().set(offset);
			
		}
		
		
	}

	/**
	 * Transform from actual space to radar space
	 * @param rt
	 */
	private void transform(Vector3f rt, float radius) {
		rt.multLocal(worldToRadarFactor);
		if (rt.lengthSquared() > 1) {
			rt.normalizeLocal();
		}
		rt.multLocal(radius);		
	}

	/**
	 * @return
	 * 		True if the radar display rotates so that up is the 
	 * 		forward axis of the center node. False if up is "north"
	 */
	public boolean isRotatedByCenter() {
		return rotatedByCenter;
	}

	/**
	 * @param rotatedByCenter
	 * 		True if the radar display rotates so that up is the 
	 * 		forward axis of the center node. False if up is "north"
	 */
	public void setRotatedByCenter(boolean rotatedByCenter) {
		this.rotatedByCenter = rotatedByCenter;
	}
	
}
