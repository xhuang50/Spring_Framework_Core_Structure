package com.redline.framework.factory;

import java.util.List;

public interface ListableBeanFactory extends BeanFactory {
    List getBeansByType(Class clazz);
}
