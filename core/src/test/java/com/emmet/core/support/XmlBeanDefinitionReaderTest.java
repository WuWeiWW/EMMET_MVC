package com.emmet.core.support;

import com.emmet.core.BeanDefinition;
import com.emmet.core.factory.HelloWorldService;
import com.emmet.core.io.ResourceLoader;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

		assertTrue(beans.size() == 5);


		assertEquals( "str1" , beans.get("str1").getBean());
		assertEquals( "string test " , beans.get("str2").getBean());
		HelloWorldService outerBean = (HelloWorldService) beans.get("outerBean").getBean();
		assertNotNull(outerBean.getMap()); //是否初始化了MAP
		assertTrue(outerBean.getMap().size() == 3); // 断言获取的时候和XMl中一致
		assertNotNull(outerBean.getProperties());
		assertTrue(outerBean.getProperties().size() == 4);
		//获取构造函数创建的Bean
		HelloWorldService contractorInit = (HelloWorldService) beans.get("contractorInit").getBean();
		assertEquals(555, contractorInit.getA());//断言是否和XML中一致
		assertEquals("666", contractorInit.getB());

		assertEquals(outerBean,contractorInit.getOuterBean()); //构造函数创建这个内部引用Bean 引用 outerBean


	}
}
