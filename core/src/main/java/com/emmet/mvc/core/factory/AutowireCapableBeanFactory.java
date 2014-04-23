package com.emmet.mvc.core.factory;

import com.emmet.mvc.core.BeanDefinition;
import com.emmet.mvc.core.PropertyValue;

import java.lang.reflect.Field;

/**
 * TODO this class description
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {

	@Override
	protected Object doCreateBean(BeanDefinition beanDefinition) throws InstantiationException, IllegalAccessException {
		Object bean = createBeanInstance(beanDefinition);
		applyPropertyValues(bean, beanDefinition);
		return bean;
	}

	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) {
		if (beanDefinition.getPropertyValues() == null) {
			return;
		}
		for (PropertyValue property : beanDefinition.getPropertyValues().getPropertyValueList()) {
			try {
				//TODO 这里应该使用set来注入
				Field field;
				field = bean.getClass().getDeclaredField(property.getName());
				field.setAccessible(true);
				field.set(bean, property.getValue());
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException("给" + beanDefinition.getBeanClass() + "注入属性失败,没找到方法或参数类型不对");
			}
		}
	}

	/**
	 * 给传入的创建Bean实例
	 *
	 * @param beanDefinition 需要创建爱你的Bean定义
	 * @return 创建后的Bean对象
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	protected Object createBeanInstance(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
		return beanDefinition.getBeanClass().newInstance();
	}
}
