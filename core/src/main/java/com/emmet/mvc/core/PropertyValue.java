package com.emmet.mvc.core;

/**
 * TODO this class description
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public class PropertyValue {

	private final String name;

	private final Object value;

	public PropertyValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}
}
