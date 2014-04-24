package com.emmet.core.support;

import com.emmet.core.AbstractBeanDefinitionReader;
import com.emmet.core.BeanDefinition;
import com.emmet.core.PropertyValue;
import com.emmet.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * XML注入Bean
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {


	public XmlBeanDefinitionReader(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}

	@Override
	public void loadBeanDefinitions(String location) {
		try {
			//获取XML文件，即将进行解析XML
			InputStream inputStream = getResourceLoader().getResource(location).getInputStream();
			doLoadBeanDefinitions(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("获取资源失败:" + location);
		}
	}

	private void doLoadBeanDefinitions(InputStream inputStream) {
		//使用w3c dom 解析
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(inputStream);
			//解析Bean
			registerBeanDefinitions(document);
			inputStream.close();//关闭文件流
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("获取Dom示例失败或加载资源文件失败");
		}
	}

	private void registerBeanDefinitions(Document document) {
		Element root = document.getDocumentElement();//获取根节点
		parseBeanDefinitions(root);//解析XML
	}

	/**
	 * 解析所有节点
	 *
	 * @param root 根节点
	 */
	private void parseBeanDefinitions(Element root) {
		NodeList childNodes = root.getChildNodes(); //获取根节点的所有子节点
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i); //获取节点
			if (item instanceof Element) {//这里过滤XML中的字符或空白
				processBeanDefinition((Element) item);
			}
		}
	}

	/**
	 * 解析Bean
	 *
	 * @param element 需要解析的Bean节点
	 */
	private void processBeanDefinition(Element element) {
		//获取XML中定义的Bean名称
		String name = element.getAttribute("name");
		//获取XML中定义的BeanClass路径
		String className = element.getAttribute("class");
		//创建一个Bean定义，初始化Bean
		BeanDefinition beanDefinition = new BeanDefinition();
		beanDefinition.setBeanClassName(className);
		try {
			beanDefinition.setBean(doCreateBean(beanDefinition));
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException("XML创建Bean对象失败,name:" + name + " value:" + className);
		}
		processProperty(element, beanDefinition);
		getRegister().put(name, beanDefinition);
	}

	/**
	 * 解析property 节点
	 *
	 * @param element        节点
	 * @param beanDefinition 需要将此属性注入到此对象上
	 */
	private void processProperty(Element element, BeanDefinition beanDefinition) {

		NodeList property = element.getElementsByTagName("property");
		for (int i = 0; i < property.getLength(); i++) {
			Node item = property.item(i);
			if (item instanceof Element) {
				Element propertyEle = (Element) item;
				String name = propertyEle.getAttribute("name");
				String value = propertyEle.getAttribute("value");
				beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
			}
		}

		applyPropertyValues(beanDefinition.getBean(),beanDefinition);

	}

	protected Object doCreateBean(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
		return beanDefinition.getBeanClass().newInstance();
	}

	/**
	 * 通过Property 给对象属性注入值
	 *
	 * @param bean           注入的对象
	 * @param beanDefinition Bean定义，这里需要里面的PropertyValues
	 */
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
}
