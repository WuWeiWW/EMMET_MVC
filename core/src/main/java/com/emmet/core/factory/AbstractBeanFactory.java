package com.emmet.core.factory;

import com.emmet.core.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean管理工厂，主要用于Bean的创建和各种Bean创建的父类
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public abstract class AbstractBeanFactory implements BeanFactory {

	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

	@Override
	public Object getBean(String name) {
		BeanDefinition beanDefinition = this.beanDefinitionMap.get(name);
		return beanDefinition == null ? null : beanDefinition.getBean();
	}

	@Override
	public BeanDefinition getBeanDefinition(String name) {
		return this.beanDefinitionMap.get(name);
	}

	@Override
	public void registerBeanDefinition(String name, BeanDefinition beanDefinition){
		if (beanDefinition.getBeanClass() == null) {
			throw new NullPointerException("Bean " + name + " 的Class不存在或未加载");
		}
		Object bean = null;
		try {
			bean = doCreateBean(beanDefinition);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		beanDefinition.setBean(bean);
		beanDefinitionMap.put(name, beanDefinition);

	}


	/**
	 * 创建Bean，让子类以自己的方式创建,可能会有XML,Annotation等方式的注入方式
	 *
	 * @param beanDefinition Bean定义的类
	 * @return 根据beanDefinition 中的 beanClassName 创建Class对象然后创建Bean对象，返回创建后的Bean
	 *
	 */
	protected abstract Object doCreateBean(BeanDefinition beanDefinition) throws InstantiationException, IllegalAccessException;
}
