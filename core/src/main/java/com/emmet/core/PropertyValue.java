package com.emmet.core;

/**
 * Property方式注入属性
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public class PropertyValue {

	//属性名
	private final String name;

	//属性值
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
