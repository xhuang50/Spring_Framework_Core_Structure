package com.redline.framework.factory.support;

import com.redline.framework.factory.ListableBeanFactory;
import com.redline.framework.ioc.BeanDefinition;
import com.redline.framework.registry.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The real concrete class to create beans
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ListableBeanFactory {
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitions.get(beanName);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitions.put(beanName, beanDefinition);
    }

    @Override
    public List<BeanDefinition> getBeanDefinition() {
        List<BeanDefinition> beanDefinitionList = new ArrayList<>();
        for (BeanDefinition bd: beanDefinitions.values()){
            beanDefinitionList.add(bd);
        }
        return beanDefinitionList;
    }

    @Override
    public List getBeansByType(Class clazz) {
        return null;
    }
}
