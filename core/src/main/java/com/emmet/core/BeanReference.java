package com.emmet.core;

/**
 * 为Bean注入Bean的实体类
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public class BeanReference {

	private String name;
	private Object bean;

	public BeanReference() {
	}

	public BeanReference(String name, Object bean) {
		this.name = name;
		this.bean = bean;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}
}
