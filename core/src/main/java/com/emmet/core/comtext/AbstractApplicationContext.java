package com.emmet.core.comtext;

import com.emmet.core.beans.factory.AbstractBeanFactory;

/**
 * TODO this class description
 * Created by EMMET on 14-4-28
 *
 * @author EMMET
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

	protected AbstractBeanFactory beanFactory;

	public AbstractApplicationContext(AbstractBeanFactory abstractBeanFactory) {
		this.beanFactory = abstractBeanFactory;
	}

	/**
	 * 实现此方法的，所有方式注入的进入类
	 * @throws Exception
	 */
	public void refresh() throws Exception{

	}

	@Override
	public Object getBean(String name) {
		return beanFactory.getBean(name);
	}
}
