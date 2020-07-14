package com.redline.framework.factory;

/**
 * Top level interface
 */
public interface BeanFactory {
    /**
     * Get the bean with its name.
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}
