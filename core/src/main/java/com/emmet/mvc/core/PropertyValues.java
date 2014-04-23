package com.emmet.mvc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO this class description
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public class PropertyValues {

	private final List<PropertyValue> propertyValueList = new ArrayList<>();

	public PropertyValues() {

	}

	/**
	 * 给当前对象添加属性
	 *
	 * @param propertyValue 对象注入的属性
	 */
	public void addPropertyValue(PropertyValue propertyValue) {
		this.propertyValueList.add(propertyValue);
	}

	public List<PropertyValue> getPropertyValueList() {
		return propertyValueList;
	}
}
