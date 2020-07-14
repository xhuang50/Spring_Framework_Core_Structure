package com.redline.springcore.test;

import com.redline.springcore.ioc.BeanDefinitionOriginal;
import com.redline.springcore.ioc.PropertyValueOriginal;
import com.redline.springcore.ioc.RuntimeBeanReferenceOriginal;
import com.redline.springcore.ioc.TypedStringValueOriginal;
import com.redline.springcore.po.User;
import com.redline.springcore.service.UserService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoV2 {
    // Store the definition of different beans
    private Map<String, BeanDefinitionOriginal> beanDefinitions = new HashMap<>();
    // Store the singleton beans
    private Map<String, Object> singletonObjects = new HashMap<>();

    @Before
    public void before(){
        String location = "beans.xml";
        InputStream inputStream = getInputStream(location);
        Document document = createDocument(inputStream);

        registerBeanDefinition(document.getRootElement());
    }

    private void registerBeanDefinition(Element rootElement) {
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            // get tag name
            String name = element.getName();
            if (name.equals("bean")) {
                // parse the <bean> tag
                parseDefaultElement(element);
            } else {
                // resolve customized tags
                parseCustomElement(element);
            }
        }
    }

    private void parseCustomElement(Element element) {
        // follow the similar way to resolve other tags such as aop, tx, mvc, etc.
    }

    private void parseDefaultElement(Element beanElement) {
        try {
            if (beanElement == null) {
                return;
            }
            // get attributes
            String id = beanElement.attributeValue("id");

            String name = beanElement.attributeValue("name");

            String clazzName = beanElement.attributeValue("class");
            if (clazzName == null || "".equals(clazzName)) {
                return;
            }

            String initMethod = beanElement.attributeValue("init-method");
            String scope = beanElement.attributeValue("scope");
            scope = scope != null && !scope.equals("") ? scope : "singleton";

            // get bean name
            String beanName = id == null ? name : id;
            Class<?> clazzType = Class.forName(clazzName);
            beanName = beanName == null ? clazzType.getSimpleName() : beanName;
            // Create the BeanDefinitionOriginal object
            // Use constructor.
            BeanDefinitionOriginal beanDefinition = new BeanDefinitionOriginal(clazzName, beanName);
            beanDefinition.setInitMethod(initMethod);
            beanDefinition.setScope(scope);
            // get property tags
            List<Element> propertyElements = beanElement.elements();
            for (Element propertyElement : propertyElements) {
                parsePropertyElement(beanDefinition, propertyElement);
            }

            // register beanDefinition into the set
            this.beanDefinitions.put(beanName, beanDefinition);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void parsePropertyElement(BeanDefinitionOriginal beanDefinition, Element propertyElement) {
        if (propertyElement == null)
            return;

        // Get attributes
        String name = propertyElement.attributeValue("name");
        String value = propertyElement.attributeValue("value");
        String ref = propertyElement.attributeValue("ref");

        // If both value and ref have contents, meaning it's already been parsed. Return
        if (value != null && !value.equals("") && ref != null && !ref.equals("")) {
            return;
        }

        /**
         * PropertyValueOriginal encapsulates a property tag info
         */
        PropertyValueOriginal pv = null;

        if (value != null && !value.equals("")) {
            // 因为spring配置文件中的value是String类型，而对象中的属性值是各种各样的，所以需要存储类型
            TypedStringValueOriginal typeStringValue = new TypedStringValueOriginal(value);

            Class<?> targetType = getTypeByFieldName(beanDefinition.getClazzName(), name);
            typeStringValue.setTargetType(targetType);

            pv = new PropertyValueOriginal(name, typeStringValue);
            beanDefinition.addPropertyValue(pv);
        } else if (ref != null && !ref.equals("")) {

            RuntimeBeanReferenceOriginal reference = new RuntimeBeanReferenceOriginal(ref);
            pv = new PropertyValueOriginal(name, reference);
            beanDefinition.addPropertyValue(pv);
        } else {
            return;
        }
    }

    private Class<?> getTypeByFieldName(String beanClassName, String name) {
        try {
            Class<?> clazz = Class.forName(beanClassName);
            Field field = clazz.getDeclaredField(name);
            return field.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Document createDocument(InputStream inputStream) {
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(inputStream);
            return document;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getInputStream(String location) {
        return this.getClass().getClassLoader().getResourceAsStream(location);
    }

    @Test
    public void test(){
        UserService userService = (UserService) getBean("userService");
        Map<String, Object> map = new HashMap<>();
        map.put("username", "Catie");
        List<User> users = userService.queryUsers(map);
        System.out.println(users);
    }

    /**
     * This method simulates the process of getting beans from spring container
     * @param beanName bean id/name
     * @return the requested bean
     */
    private Object getBean(String beanName) {
        // As the bean identifier is given as a string
        // 1. check the bean collection, if the bean is already created then return it.
        Object bean = singletonObjects.get(beanName);
        if (bean != null) {
            return bean;
        }
        // 2. otherwise, create the bean with info from definition collection
        BeanDefinitionOriginal beanDefinition = beanDefinitions.get(beanName);
        if (beanDefinition == null){
            return null;
        }
        bean = createBean(beanDefinition);
        // 3. after creation, if the bean is defined as singleton, store it into the bean collection
        if (beanDefinition.isSingleton()){
            singletonObjects.put(beanName, bean);
        }
        return bean;
    }

    /**
     * Create a bean with its definition
     * @param beanDefinition bean structure
     * @return
     */
    private Object createBean(BeanDefinitionOriginal beanDefinition) {
        // Three steps:
        // 1. Instantiate
        Object bean = createBeanInstance(beanDefinition);
        // 2. value/reference injection from the definition
        populateBean(bean, beanDefinition);
        // 3. Initialization. Call init() method
        initializeBean(bean, beanDefinition);

        return bean;
    }

    /**
     * invoke the initialization process
     * @param bean
     * @param beanDefinition
     */
    private void initializeBean(Object bean, BeanDefinitionOriginal beanDefinition) {
        //invoke init() method
        invokeInitMethod(bean, beanDefinition);
    }

    private void invokeInitMethod(Object bean, BeanDefinitionOriginal beanDefinition) {
        try{
            String initMethod = beanDefinition.getInitMethod();
            if (initMethod == null || "".equals(initMethod)){
                return;
            }
            Class<?> clazzType = beanDefinition.getClazzType();
            Method method = clazzType.getDeclaredMethod(initMethod);
            method.invoke(bean);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void populateBean(Object bean, BeanDefinitionOriginal beanDefinition) {
        List<PropertyValueOriginal> propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValueOriginal pv : propertyValues) {
            // get property name and value
            String name = pv.getName();
            Object value = pv.getValue();
            // wrap the value
            Object wrappedValue = resolveValue(value);

            setPropertyValue(bean, name, wrappedValue, beanDefinition);



        }
    }

    private void setPropertyValue(Object bean, String name, Object wrappedValue, BeanDefinitionOriginal beanDefinition) {
        try{
            Class<?> clazzType = beanDefinition.getClazzType();
            Field field = clazzType.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean,wrappedValue);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Object resolveValue(Object value) {
        if (value instanceof TypedStringValueOriginal) {
            TypedStringValueOriginal typedStringValue = (TypedStringValueOriginal) value;
            String stringValue = typedStringValue.getValue();
            Class<?> targetType = typedStringValue.getTargetType();
            // is there a cleaner way?
            if (targetType == String.class){
                return stringValue;
            }else if (targetType == Integer.class){
                return Integer.parseInt(stringValue);
            } else if (targetType == Long.class){
                return Long.parseLong(stringValue);
            }else if (targetType == Float.class){
                return Float.parseFloat(stringValue);
            } else if (targetType == Double.class){
                return Double.parseDouble(stringValue);
            }else if (targetType == Byte.class){
                return Byte.parseByte(stringValue);
            } else if (targetType == Short.class){
                return Short.parseShort(stringValue);
            }else if (targetType == Boolean.class){
                return Boolean.parseBoolean(stringValue);
            }
        } else if (value instanceof RuntimeBeanReferenceOriginal){
            RuntimeBeanReferenceOriginal runtimeBeanReference = (RuntimeBeanReferenceOriginal) value;
            String ref = runtimeBeanReference.getRef();
            // recursively get the referred bean
            return getBean(ref);
        }
        return null;
    }

    /**
     * Spring has three ways to create a new bean
     * @param beanDefinition
     * @return
     */
    private Object createBeanInstance(BeanDefinitionOriginal beanDefinition) {
        // 1. Factory static method
        // 2. Factory object
        // 3. Constructor
        Object bean = createBeanByConstructor(beanDefinition);
        return bean;
    }

    /**
     * invoke constructor through Java reflection
     * @param beanDefinition
     * @return
     */
    private Object createBeanByConstructor(BeanDefinitionOriginal beanDefinition) {
        Class<?> clazzType = beanDefinition.getClazzType();
        try {
            Constructor<?> constructor = clazzType.getDeclaredConstructor();
            Object bean = constructor.newInstance();
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
