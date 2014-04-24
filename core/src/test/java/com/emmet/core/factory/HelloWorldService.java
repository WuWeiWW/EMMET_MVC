package com.emmet.core.factory;

import java.util.List;
import java.util.Map;

/**
 * 测试注入的对象
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public class HelloWorldService {

	private String text;

	private HelloWorldService helloWorldService;

	private List<Object> objects;

	private Map<String,Object> stringObjectMap;

	private int intA;

	private double aDouble;

	public HelloWorldService(int intA, String text) {
		this.intA = intA;
		this.text = text;
	}

	public HelloWorldService() {
	}

	public void sayHello() {
		System.out.println(text);
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public HelloWorldService getHelloWorldService() {
		return helloWorldService;
	}

	public void setHelloWorldService(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}

	public List<Object> getObjects() {
		return objects;
	}

	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}

	public Map<String, Object> getStringObjectMap() {
		return stringObjectMap;
	}

	public void setStringObjectMap(Map<String, Object> stringObjectMap) {
		this.stringObjectMap = stringObjectMap;
	}

	public int getIntA() {
		return intA;
	}

	public void setIntA(int intA) {
		this.intA = intA;
	}

	public double getaDouble() {
		return aDouble;
	}

	public void setaDouble(double aDouble) {
		this.aDouble = aDouble;
	}
}
