package net.java.dev.aircarrier.hud;

import net.java.dev.aircarrier.util.NamedValuesBean;

import com.jme.scene.Controller;

/**
 * Control a gauge to show a value from a bean
 * @author goki
 */
public class GaugeController extends Controller {
	private static final long serialVersionUID = 8720962534725579344L;
	
	Gauge gauge;
	NamedValuesBean bean;
	String valueName;
	float factor;

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
	 */
	public GaugeController(Gauge gauge, NamedValuesBean bean, String valueName, float factor) {
		super();
		this.gauge = gauge;
		this.bean = bean;
		this.valueName = valueName;
		this.factor = factor;
	}

	@Override
	public void update(float time) {
		gauge.setValue(bean.getNamedValue(valueName) * factor);
	}

}
