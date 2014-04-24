package com.emmet.core;

/**
 * 封装Bean，可以保存额外的源数据,保存在BeanFactory中，用于包装Bean的实体
 * 这样封装后，为自动注入提供标准
 * @author EMMET
 */
public class BeanDefinition {

	private Object bean; // Bean对象

	private Class beanClass; // Bean对应的Class对象

	private String beanClassName; // bean的Class路径

	private PropertyValues propertyValues = new PropertyValues();

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public Class getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class beanClass) {
		this.beanClass = beanClass;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
		try {
			this.beanClass = Class.forName(beanClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public BeanDefinition() {

	}

	public BeanDefinition(String beanClassName) {
		this.setBeanClassName(beanClassName);
	}

	public Object getBean() {
		return this.bean;
	}

	public PropertyValues getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}
}
