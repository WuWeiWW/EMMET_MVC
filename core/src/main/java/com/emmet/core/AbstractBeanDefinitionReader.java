package com.emmet.core;

import com.emmet.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 读取资源文件中 子类实现读取的定义
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	private Map<String,BeanDefinition> register;

	private ResourceLoader resourceLoader;

	public AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
		this.register = new HashMap<>(); //只允许创建一次
		this.resourceLoader = resourceLoader;
	}

	/**
	 * 所有的注入Bean
	 * @return BeanMap
	 */
	public Map<String, BeanDefinition> getRegister() {
		return register;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

}
