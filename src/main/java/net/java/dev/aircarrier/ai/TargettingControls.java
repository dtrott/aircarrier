package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.controls.PlaneControls;

public interface TargettingControls extends PlaneControls{

	public Acobject getTarget();

	public void setTarget(Acobject target);

}