package com.emmet.core.support;

import com.emmet.core.BeanDefinition;
import com.emmet.core.factory.HelloWorldService;
import com.emmet.core.io.ResourceLoader;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 测试XML注入Bean
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public class XmlBeanDefinitionReaderTest {
	@Test
	public void testLoadBeanDefinitions() throws Exception {
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(new ResourceLoader());
		beanDefinitionReader.loadBeanDefinitions("beans.xml");//加载XML并解析Bean
		Map<String,BeanDefinition> beans = beanDefinitionReader.getRegister();//获得所有Bean
		assertTrue(beans.size() > 0);
		Object helloWorldServiceO = beans.get("helloWorldService").getBean();
		assertTrue( helloWorldServiceO instanceof HelloWorldService);
		HelloWorldService helloWorldService = (HelloWorldService)helloWorldServiceO;
		assertEquals("hello world !!!" , helloWorldService.getText()); //断言xml中注入的和取出的是否一致
		helloWorldService.sayHello();

	}
}
