package net.java.dev.aircarrier.triggers;

import java.util.List;

import net.java.dev.aircarrier.acobject.Acobject;

import com.jme.scene.Controller;

/**
 * Updates a trigger, checking whether it is triggered
 * by an object in a list, and updating the trigger itself
 * @author shingoki
 *
 */
public class TriggerListController extends Controller {
	private static final long serialVersionUID = 393067038269323692L;
	
	List<Acobject> objects;
	Trigger trigger;
	
	public TriggerListController(Trigger trigger, List<Acobject> objects) {
		super();
		this.trigger = trigger;
		this.objects = objects;
	}

	@Override
	public void update(float time) {
		trigger.update(time);
		for (Acobject object : objects) {
			trigger.check(object);
		}
	}

}
