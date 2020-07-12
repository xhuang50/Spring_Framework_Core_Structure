package com.redline.springcore.test;

import com.redline.springcore.ioc.BeanDefinition;
import com.redline.springcore.po.User;
import com.redline.springcore.service.UserService;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoV2 {
    // Store the definition of different beans
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    // Store the singleton beans
    private Map<String, Object> singletonObjects = new HashMap<>();

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
        BeanDefinition beanDefinition = beanDefinitions.get(beanName);
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
     * @param beanDefinition
     * @return
     */
    private Object createBean(BeanDefinition beanDefinition) {
        // Three steps:
        // 1. Instantiate
        Object bean = createBeanInstance(beanDefinition);
        // 2. value/reference injection from the definition
        populateBean(bean, beanDefinition);
        // 3. Initialization. Call init() method
        initializeBean(bean, beanDefinition);

        return bean;
    }

    private void initializeBean(Object bean, BeanDefinition beanDefinition) {

    }

    private void populateBean(Object bean, BeanDefinition beanDefinition) {

    }

    /**
     * Spring has three ways to create a new bean
     * @param beanDefinition
     * @return
     */
    private Object createBeanInstance(BeanDefinition beanDefinition) {
        // 1. Factory static method
        // 2. Factory object
        // 3. Constructor

        return null;
    }


}
