package com.redline.framework.registry;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulate the singleton beans
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

    @Override
    public void addSingleton(String beanName, Object bean) {
        // TODO Ensure the the bean is singleton in the map
        synchronized (singletonObjects){
            this.singletonObjects.put(beanName, bean);
        }
    }
}
