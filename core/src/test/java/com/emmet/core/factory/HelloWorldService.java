package com.emmet.core.factory;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * 测试注入的对象
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public class HelloWorldService {

	private int a;
	private Integer aa;
	private String b;
	private HelloWorldService innerBean;

	private HelloWorldService outerBean;

	private Collection<String> collection;

	private Map<String,String> map;
	private Properties properties;


	public HelloWorldService() {
	}

	public HelloWorldService(int a, String b, HelloWorldService outerBean) {
		this.a = a;
		this.b = b;
		this.outerBean = outerBean;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public Integer getAa() {
		return aa;
	}

	public void setAa(Integer aa) {
		this.aa = aa;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public HelloWorldService getInnerBean() {
		return innerBean;
	}

	public void setInnerBean(HelloWorldService innerBean) {
		this.innerBean = innerBean;
	}

	public HelloWorldService getOuterBean() {
		return outerBean;
	}

	public void setOuterBean(HelloWorldService outerBean) {
		this.outerBean = outerBean;
	}

	public Collection<String> getCollection() {
		return collection;
	}

	public void setCollection(Collection<String> collection) {
		this.collection = collection;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
