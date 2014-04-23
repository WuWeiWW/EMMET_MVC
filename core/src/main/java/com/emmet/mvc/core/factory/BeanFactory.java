package com.emmet.mvc.core.factory;

import com.emmet.mvc.core.BeanDefinition;

/**
 * 容器
 *
 * @author EMMET
 */
public interface BeanFactory {

	/**
	 * 根据Bean的名字获取Bean对象
	 *
	 * @param name Bean的名字
	 * @return Bean对象，如果Bean不存在，则返回null
	 */
	public Object getBean(String name);


	/**
	 * 根据Bean的名字获得Bean的定义，包括Class等信息
	 *
	 * @param name Bean的名字
	 * @return Bean定义对象，如果Bean不存在，则返回null
	 */
	public BeanDefinition getBeanDefinition(String name);

	/**
	 * 注册一个Bean
	 *
	 * @param name           要注册Bean的名字
	 * @param beanDefinition Bean对象
	 */
	public void registerBeanDefinition(String name, BeanDefinition beanDefinition);
}
