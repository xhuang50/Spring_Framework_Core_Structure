package com.redline.framework.resolver;

import com.redline.framework.factory.BeanFactory;
import com.redline.framework.ioc.RuntimeBeanReference;
import com.redline.framework.ioc.TypedStringValue;

public class BeanDefinitionValueResolver {
    private BeanFactory beanFactory;

    public BeanDefinitionValueResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValue(Object value) {
        if (value instanceof TypedStringValue){
            TypedStringValue typedStringValue = (TypedStringValue) value;
            String stringValue = typedStringValue.getValue();
            Class<?> targetType = typedStringValue.getTargetType();
            if (targetType == String.class){
                return stringValue;
            }else if (targetType == Integer.class){
                return Integer.parseInt(stringValue);
            }//....
        }else if (value instanceof RuntimeBeanReference){
            RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;
            String ref = runtimeBeanReference.getRef();
            return beanFactory.getBean(ref);
        }
        return null;
    }
}
