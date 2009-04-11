package net.java.dev.aircarrier.hud;

import net.java.dev.aircarrier.util.FloatSpring;
import net.java.dev.aircarrier.util.NamedValuesBean;

import com.jme.scene.Controller;

/**
 * Control a gauge to show a value from a bean, with
 * a springy link between bean value and displayed value,
 * to simulate a mechanical or sensor delay in the gauge.
 * @author goki
 */
public class SpringyGaugeController extends Controller {
	private static final long serialVersionUID = 8720962534725579344L;
	
	Gauge gauge;
	NamedValuesBean bean;
	String valueName;
	float factor;
	FloatSpring spring;

	/**
	 * Make a controller, which updates a specified gauge
	 * to show the named value of a bean
	 * This uses a default spring with a subsecond response time 
	 * for unit changes, suitable for most purposes, particularly
	 * for displaying values expected to take integer values which
	 * do not change at a very large rate per second (e.g. ammunition count)
	 * @param gauge
	 * 		The gauge to control
	 * @param bean
	 * 		The bean whose value will be displayed on the gauge
	 * @param valueName
	 * 		The name of the value of the bean to be displayed
	 * @param factor
	 * 		The factor by which to multiply the value before display
	 */
	public SpringyGaugeController(Gauge gauge, NamedValuesBean bean, String valueName, float factor) {
		this(gauge, bean, valueName, factor, new FloatSpring(100));
	}
	
	/**
	 * Make a controller, which updates a specified gauge
	 * to show the named value of a bean
	 * @param gauge
	 * 		The gauge to control
	 * @param bean
	 * 		The bean whose value will be displayed on the gauge
	 * @param valueName
	 * 		The name of the value of the bean to be displayed
	 * @param factor
	 * 		The factor by which to multiply the value before display
	 * @param spring
	 * 		The spring used to translate bean values into (delayed)
	 * 		gauge readout values.
	 */
	public SpringyGaugeController(Gauge gauge, NamedValuesBean bean, String valueName, float factor, FloatSpring spring) {
		super();
		this.gauge = gauge;
		this.bean = bean;
		this.valueName = valueName;
		this.factor = factor;
		this.spring = spring;
	}

	@Override
	public void update(float time) {
		float newValue = bean.getNamedValue(valueName) * factor;
		spring.update(newValue, time);
		gauge.setValue(spring.getPosition());
	}

}
