package com.emmet.mvc.core.factory;

import com.emmet.mvc.core.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO this class description
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
	 * 创建Bean，让之类以自己的方式创建
	 *
	 * @param beanDefinition
	 * @return
	 */
	protected abstract Object doCreateBean(BeanDefinition beanDefinition) throws InstantiationException, IllegalAccessException;
}
