package com.emmet.core;

/**
 * 从配置中读取Bean
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public interface BeanDefinitionReader {

	void loadBeanDefinitions(String location);
}
