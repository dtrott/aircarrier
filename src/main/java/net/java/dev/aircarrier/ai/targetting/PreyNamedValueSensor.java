package net.java.dev.aircarrier.ai.targetting;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.acobject.NamedValuesAcobject;

public class PreyNamedValueSensor implements TargetChoiceSensor<Acobject, NamedValuesAcobject> {

	String valueName;
	
	public PreyNamedValueSensor(String valueName) {
		super();
		this.valueName = valueName;
	}

	public float getTargettingValue(Acobject hunter, NamedValuesAcobject prey) {
		return prey.getNamedValue(valueName);
	}

	public void update(float time) {
		//Not time dependent
	}

	public void targettingChange(Acobject hunter, NamedValuesAcobject oldPrey, NamedValuesAcobject newPrey) {
		//Not dependent on actual targetting
	}

}
