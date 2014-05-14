package com.emmet.core.beans.support;

import com.emmet.core.beans.AbstractBeanDefinitionReader;
import com.emmet.core.beans.BeanDefinition;
import com.emmet.core.beans.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * XML注入Bean
 * Created by EMMET on 14-4-24
 *
 * @author EMMET
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {


	private Document document;//beans XML文档


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
			document = documentBuilder.parse(inputStream);
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


		for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling()) {
			if (node.getNodeName().equals("bean") //如果是延迟加载类型，不进行初始化
					&& ((Element) node).hasAttribute("lazy-init")
					&& ((Element) node).getAttribute("lazy-init").equals("true")
					) {
				continue;
			}

			//是节点类型才进行解析
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element beanElement = (Element) node;
				String name; //如果Bean节点上没有 Id属性，那么就用它的Class来作为标识
				if (beanElement.hasAttribute("id")) {
					name = beanElement.getAttribute("id");
				} else {
					name = beanElement.getAttribute("class");
				}
				//放到BeanMap里
				getRegister().put(name, processBeanDefinition(beanElement));
			}
		}
	}

	/**
	 * 解析Bean
	 *
	 * @param element 需要解析的Bean节点
	 */
	private BeanDefinition processBeanDefinition(Element element) {
		String className = element.getAttribute("class");
		if (className == null) {
			throw new NullPointerException("初始化Bean，Class为空" + element.toString());
		}
		//Bean定义对象
		BeanDefinition beanDefinition = new BeanDefinition(className);
		Object beanObject = null; //Bean对象
		try {
			//创建Bean实例
			beanObject = doCreateBean(beanDefinition);
			beanDefinition.setBean(beanObject);
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("实例化Bean对象失败:" + className);
		}

		//过滤基本数据类型，如果Bean的属性是基本数据类型
		if (isPrimitive(beanDefinition.getBeanClass()) && element.hasAttribute("value")) {
			beanObject = getInstanceForName(className, element.getAttribute("value"));
			beanDefinition.setBean(beanObject);
			return beanDefinition;
		}

		//是否已构造方式注入
		boolean isContractor = false;
		ArrayList<Class> parameterTypes = new ArrayList<Class>(); //构造函数参数类型
		ArrayList<Object> parameters = new ArrayList<Object>(); //构造函数参数值
		for (Node property = element.getFirstChild(); property != null; property = property.getNextSibling()) {
			if (property.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element elementProperty = (Element) property;
			//确定是已Contractor
			if (elementProperty.getNodeName().equals("constructor-arg")) {
				isContractor = true;
				if (elementProperty.hasAttribute("value")) {
					Class clazz = nameToPrimitiveClass(elementProperty.getAttribute("type"));
					Object constructorParameter = getInstanceForName(elementProperty.getAttribute("type"), elementProperty.getAttribute("value"));
					parameterTypes.add(clazz);
					parameters.add(constructorParameter);
				} else if (elementProperty.hasAttribute("ref")) {
					String refId = elementProperty.getAttribute("ref");
					//是否已经初始化过此Bean
					if (getRegister().containsKey(refId)) {
						//已经初始化过，直接添加
						BeanDefinition constructorParameter = getRegister().get(refId);
						parameterTypes.add(constructorParameter.getBeanClass());
						parameters.add(constructorParameter.getBean());
					} else {
						//未初始化过
						Element elementById = document.getElementById(refId);
						if (elementById.getNodeName().equals("bean")) {
							BeanDefinition constructorParameterBean = processBeanDefinition(elementById);
							parameterTypes.add(constructorParameterBean.getBeanClass());
							parameters.add(constructorParameterBean.getBean());
						}
					}
				}
			} else if (elementProperty.getNodeName().equals("property")) {
				//以属性方式注入
				String elementVName = elementProperty.getAttribute("name");//获取property 节点上的name
				Method[] methods = beanDefinition.getBeanClass().getMethods();
				Method method = null;
				for (Method tempMethod : methods) {
					if (tempMethod.getName().equalsIgnoreCase("set" + elementVName)) {
						method = tempMethod;
						break;
					}
				}
				if (method == null) {
					throw new NullPointerException("没有找到Bean：beanDefinition.getBean() 的" + elementVName + "方法");
				}
				Object propertyValue = null;//Bean属性的值 ，Property 注入的属性值
				if (elementProperty.hasAttribute("value")) {
					// 该属性为直接属性，非外部bean引用属性
					String value = elementProperty.getAttribute("value");
					//methodParameterTypes[0]  set方法正常情况下就一个参数，如果Set方法第一个参数的Class类型,然后创建对象
					propertyValue = getInstanceForName(method.getParameterTypes()[0].getName(), value);
				} else if (elementProperty.hasAttribute("ref")) {//Bean下引用别的Bean
					String refId = elementProperty.getAttribute("ref");
					if (getRegister().containsKey(refId)) { //已经初始化过此Bean
						propertyValue = getRegister().get(refId).getBean();
					} else {
						//未初始化
						Element propertyValueElement = document.getElementById(refId);
						propertyValue = processBeanDefinition(propertyValueElement).getBean();
					}
				} else if (containNode(elementProperty, "bean")) {//Bean下内部Bean
					//此Bean下是否还包含Bean
					Node innerNode;
					for (innerNode = elementProperty.getFirstChild(); innerNode != null; innerNode = innerNode.getNextSibling()) {
						if (innerNode.getNodeType() == Node.ELEMENT_NODE && innerNode.getNodeName().equals("bean")) {
							break;
						}
					}
					BeanDefinition innerBeanDefinition = processBeanDefinition((Element) innerNode);
					propertyValue = innerBeanDefinition.getBean();
				} else if (containNode(elementProperty, "list") || containNode(elementProperty, "set")) {//Bean下Set 或List集合
					//如果是以list 或set 注入
					Collection<Object> collection = containNode(elementProperty, "list") ? new ArrayList<>() : new HashSet<>();

//					<property name="collection">
//					<list>
//					<ref bean="str1"/> getFirstChild(getFirstChild(elementProperty)) 获取子节点的子节点 ，定位到这里，下面Map,Prpos 同理
//					<ref bean="str2"/>
//					</list>
//					</property>
					for (Node currentNode = getFirstChild(getFirstChild(elementProperty)); currentNode != null; currentNode = currentNode.getNextSibling()) {
						if (currentNode.getNodeType() != Node.ELEMENT_NODE) {
							continue;
						}
						Element currentElement = (Element) currentNode;
						if (currentNode.getNodeName().equals("ref")
								&& currentElement.hasAttribute("bean")
								&& getRegister().containsKey(currentElement.getAttribute("bean"))
								) {
							String ref = currentElement.getAttribute("bean");
							collection.add(getRegister().get(ref).getBean());
						} else {
							//
							collection.add(processBeanDefinition(currentElement).getBean());
						}
					}
					propertyValue = collection;
				} else if (containNode(elementProperty, "map")) {
					HashMap<String, Object> map = new HashMap<>();
					for (Node currentNode = getFirstChild(getFirstChild(elementProperty)); currentNode != null; currentNode = currentNode.getNextSibling()) {
						if (currentNode.getNodeType() != Node.ELEMENT_NODE) {
							continue;
						}
						Element currentElement = (Element) currentNode;

						if (currentElement.getNodeName().equals("entry")
								&& currentElement.hasAttribute("key")) {
							String key = currentElement.getAttribute("key");
							if (currentElement.hasAttribute("value-ref")
									&& getRegister().containsKey(currentElement.getAttribute("value-ref"))) {
								String ref = currentElement.getAttribute("value-ref");
								map.put(key, getRegister().get(ref).getBean());
							} else if (currentElement.hasAttribute("value")) {
								map.put(key, currentElement.getAttribute("value"));
							}
						}
					}
					propertyValue = map;
				} else if (containNode(elementProperty, "props")) {
					Properties props = new Properties();
					for (Node currentNode = getFirstChild(getFirstChild(elementProperty)); currentNode != null; currentNode = currentNode.getNextSibling()) {
						if (currentNode.getNodeType() != Node.ELEMENT_NODE) {
							continue;
						}
						Element currentElement = (Element) currentNode;
						if (currentElement.getNodeName().equals("prop") && currentElement.hasAttribute("key")) {
							String key = currentElement.getAttribute("key");
							String propValue = currentElement.getTextContent();
							props.setProperty(key, propValue);
						}
					}
					propertyValue = props;
				}
				//调用Set方法，传入参数
				try {
					method.invoke(beanDefinition.getBean(), propertyValue);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		//若为构造函数创建
		if (isContractor) {
			Class[] aClass = {};
			Class[] classes = parameterTypes.toArray(aClass);
			Object[] prams = parameters.toArray();
			try {
				Constructor constructor = beanDefinition.getBeanClass().getConstructor(classes);
				beanDefinition.setBean(constructor.newInstance(prams));
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
				throw new RuntimeException("通过构造函数创建Bean失败:" + beanDefinition.getBeanClass() + "," + e.getMessage());
			}
		}
		//返回创建后的Bean定义，包含了Bean对象
		return beanDefinition;
	}

	/**
	 * 查看节点下是否存在某个节点
	 * @param elementProperty 源节点
	 * @param targetElement 目标节点
	 * @return 如果找到返回true，否则返回false
	 */
	private boolean containNode(Element elementProperty, String targetElement) {
		for (Node currentNode = elementProperty.getFirstChild(); currentNode != null; currentNode = currentNode.getNextSibling()) {
			if (currentNode.getNodeName().equals(targetElement)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过Class Name转成Class对象,如果是基本数据类型，使用包装类
	 * @param name class名字
	 * @return 创建后的Class对象，如果创建失败，返回null
	 */
	private Class nameToPrimitiveClass(String name) {
		Class clazz = null;
		switch (name) {
			case "int":
				clazz = int.class;
				break;
			case "char":
				clazz = char.class;
				break;
			case "boolean":
				clazz = boolean.class;
				break;
			case "short":
				clazz = short.class;
				break;
			case "long":
				clazz = long.class;
				break;
			case "float":
				clazz = float.class;
				break;
			case "double":
				clazz = double.class;
				break;
			case "byte":
				clazz = byte.class;
				break;
			default:
				try {
					clazz = Class.forName(name);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
		}
		return clazz;
	}

	/**
	 * 通过Class名字获取构造函数，并创建对象
	 * @param className class名字
	 * @param value 已构造函数创建传入的值
	 * @return 已构造函数创建的结果
	 */
	private Object getInstanceForName(String className, String value) {
		Class clazz = nameToClass(className);
		try {
			return clazz.getConstructor(String.class).newInstance(value);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Class nameToClass(String className) {
		Class clazz = null;
		switch (className) {
			case "int":
				clazz = Integer.class;
				break;
			case "char":
				clazz = Character.class;
				break;
			case "boolean":
				clazz = Boolean.class;
				break;
			case "short":
				clazz = Short.class;
				break;
			case "long":
				clazz = Long.class;
				break;
			case "float":
				clazz = Float.class;
				break;
			case "double":
				clazz = Double.class;
				break;
			case "byte":
				clazz = Byte.class;
				break;
			default:
				try {
					clazz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
		}
		return clazz;
	}

	/**
	 * 是否为基本数据类型
	 * @param className 要判断的Class对象
	 * @return 如果是基本数据类型返回true，否则返回false
	 */
	private boolean isPrimitive(Class className) {
		String name = className.getName();
		return className.isPrimitive()
				|| name.equals("java.lang.String")
				|| name.equals("java.lang.Integer")
				|| name.equals("java.lang.Short")
				|| name.equals("java.lang.Double")
				|| name.equals("java.lang.Float")
				|| name.equals("java.lang.Byte")
				|| name.equals("java.lang.Boolean")
				|| name.equals("java.lang.Character");
	}

	protected Object doCreateBean(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
		return beanDefinition.getBeanClass().newInstance();
	}

	private Node getFirstChild(Node currentNode) {
		return currentNode.getFirstChild().getNodeType() == Node.ELEMENT_NODE ? currentNode.getFirstChild() : currentNode.getFirstChild().getNextSibling();
	}

}
