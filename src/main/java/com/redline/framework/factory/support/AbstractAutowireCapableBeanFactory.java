package com.redline.framework.factory.support;

import com.redline.framework.ReflectUtils;
import com.redline.framework.factory.AutowireCapableBeanFactory;
import com.redline.framework.ioc.BeanDefinition;
import com.redline.framework.ioc.PropertyValue;
import com.redline.framework.resolver.BeanDefinitionValueResolver;

import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    @Override
    protected Object createBean(BeanDefinition beanDefinition) {
        Object bean = createBeanInstance(beanDefinition);
        populateBean(bean, beanDefinition);
        initializeBean(bean, beanDefinition);
        return bean;
    }

    private void initializeBean(Object bean, BeanDefinition beanDefinition) {
        invokeInitMethod(bean,beanDefinition);
    }

    private void invokeInitMethod(Object bean, BeanDefinition beanDefinition) {
        try{
            String initMethod = beanDefinition.getInitMethod();
            if (initMethod == null || "".equals(initMethod)){
                return;
            }

            ReflectUtils.invoke(beanDefinition.getClazzType(),initMethod, bean);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void populateBean(Object bean, BeanDefinition beanDefinition) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue pv : propertyValues) {
            String name = pv.getName();
            Object value = pv.getValue();

            BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
            Object resolvedValue = valueResolver.resolveValue(value);

            ReflectUtils.setPropertyValue(bean, name, resolvedValue, beanDefinition.getClazzType());
        }
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        return ReflectUtils.newInstance(beanDefinition.getClazzType());
    }
}
