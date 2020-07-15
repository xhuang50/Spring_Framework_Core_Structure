package com.redline.framework.registry;

import com.redline.framework.ioc.BeanDefinition;

import java.util.List;

/**
 * Encapsulate the Bean Definitions, or bean structures.
 */
public interface BeanDefinitionRegistry {
    /**
     * get bean definition based on bean name
     * @param beanName
     * @return
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * Register the new bean definition
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    List<BeanDefinition> getBeanDefinition();
}
