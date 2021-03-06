package com.emmet.core.factory;

import com.emmet.core.beans.BeanDefinition;
import com.emmet.core.beans.PropertyValue;
import com.emmet.core.beans.PropertyValues;
import com.emmet.core.beans.factory.BeanFactory;
import com.emmet.core.beans.support.AutowireCapableBeanFactory;
import org.junit.Test;

/**
 * BeanFactory测试类
 * Created by EMMET on 14-4-23
 *
 * @author EMMET
 */
public class BeanFactoryTest {


	@Test
	public void testRegisterBeanDefinition() {
		//初始化BeanFactory
		BeanFactory beanFactory = new AutowireCapableBeanFactory();
		//创建需要注入的Bean
		BeanDefinition beanDefinition = new BeanDefinition("com.emmet.core.factory.HelloWorldService");

		//设置属性
		PropertyValues propertyValues = new PropertyValues();
		propertyValues.addPropertyValue(new PropertyValue("text", "HelloWorld"));
		beanDefinition.setPropertyValues(propertyValues);
		//注入Bean
		String beanName = "helloWorldService";
//		beanFactory.registerBeanDefinition(beanName, beanDefinition);

		//获取Bean
		HelloWorldService helloWorldService = (HelloWorldService) beanFactory.getBean(beanName);
//		helloWorldService.sayHello();
//		System.out.println("通过属性注入的数据：" + helloWorldService.getText());

		//获得Bean定义
		/*System.out.println("*******通过获得Bean定义输出:");
		BeanDefinition beanDefinition1 = beanFactory.getBeanDefinition(beanName);
		System.out.println("class对象：" + beanDefinition1.getBeanClass());
		System.out.println("class路径:" + beanDefinition1.getBeanClassName());
		((HelloWorldService) beanDefinition1.getBean()).sayHello();*/

	}

}
