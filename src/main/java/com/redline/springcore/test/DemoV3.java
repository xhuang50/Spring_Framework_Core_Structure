package com.redline.springcore.test;

import com.redline.framework.factory.support.DefaultListableBeanFactory;
import com.redline.framework.reader.XmlBeanDefinitionReader;
import com.redline.framework.resource.ClasspathResource;
import com.redline.framework.resource.Resource;
import com.redline.springcore.po.User;
import com.redline.springcore.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoV3 {

    private DefaultListableBeanFactory beanFactory;

    @Before
    public void before(){
        String location = "beans.xml";
        Resource resource = new ClasspathResource(location);
        InputStream inputStream = resource.getResource();

        beanFactory = new DefaultListableBeanFactory();

        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.registerBeanDefinitions(inputStream);
    }

    @Test
    public void test(){
        UserService userService = (UserService) beanFactory.getBean("userService");
        Map<String, Object> map = new HashMap<>();
        map.put("username", "Catie");
        List<User> users = userService.queryUsers(map);
        System.out.println(users);
    }
}
