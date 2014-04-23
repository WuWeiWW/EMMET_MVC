package com.emmet.mvc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性注入的wrap,便于将来扩展
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public class PropertyValues {

	//当前对象所有的将注入的属性值
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
