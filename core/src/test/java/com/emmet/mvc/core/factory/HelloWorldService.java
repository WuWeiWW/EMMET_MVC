package com.emmet.mvc.core.factory;

/**
 * TODO this class description
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public class HelloWorldService {

	private String text;

	public void sayHello() {
		System.out.println("hello world");
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
