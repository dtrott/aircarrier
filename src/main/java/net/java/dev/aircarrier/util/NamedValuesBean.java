package net.java.dev.aircarrier.util;

import java.util.List;

/**
 * For a bean having a set of named values
 * @author goki
 */
public interface NamedValuesBean {

	/**
	 * @return
	 * 		A list of the names of values
	 */
	public List<String> getValueNames();
	
	/**
	 * The value of the named property, or 0 if that property does not exist
	 * @param name
	 * 		The name of the property
	 * @return
	 * 		The value, or 0 for non-existent values
	 */
	public float getNamedValue(String name);
	
}
