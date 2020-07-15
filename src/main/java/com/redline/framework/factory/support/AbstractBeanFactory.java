package com.redline.framework.factory.support;

import com.redline.framework.factory.BeanFactory;
import com.redline.framework.ioc.BeanDefinition;
import com.redline.framework.registry.DefaultSingletonBeanRegistry;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String beanName) {
        // 1. check the bean collection, if the bean is already created then return it.
        Object bean = getSingleton(beanName);
        if (bean != null) {
            return bean;
        }
        // 2. otherwise, create the bean with info from definition collection
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (beanDefinition == null){
            return null;
        }
        bean = createBean(beanDefinition);
        // 3. after creation, if the bean is defined as singleton, store it into the bean collection
        if (beanDefinition.isSingleton()){
            addSingleton(beanName, bean);
        }
        return bean;
    }

    /**
     * Ask the child class to implement
     * Abstract model design pattern
     * @param beanName
     * @return
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName);


    /**
     * Ask the child class to get bean definition
     * @param beanDefinition
     * @return
     */
    protected abstract Object createBean(BeanDefinition beanDefinition);

}
