package com.emmet.core.comtext;

import com.emmet.core.beans.factory.AbstractBeanFactory;
import com.emmet.core.beans.support.AutowireCapableBeanFactory;

/**
 * TODO this class description
 * Created by EMMET on 14-4-28
 *
 * @author EMMET
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

	private String configLocation;

	public ClassPathXmlApplicationContext(String configLocation) throws Exception {
		this(configLocation, new AutowireCapableBeanFactory());
	}

	public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory) throws Exception {
		super(beanFactory);
		this.configLocation = configLocation;
		refresh();
	}

	@Override
	public void refresh() throws Exception {
	}
}
