package com.redline.springcore.ioc;

import java.util.ArrayList;
import java.util.List;

public class BeanDefinitionOriginal {
    private String clazzName;
    private Class<?> clazzType;
    // bean identifier/name
    private String beanName;
    private String initMethod;
    // default singleton
    private String scope;

    private List<PropertyValueOriginal> propertyValues = new ArrayList<>();

    private static final String SCOPE_SINGLETON = "singleton";
    private static final String SCOPE_PROTOTYPE = "prototype";

    public BeanDefinitionOriginal(String clazzName, String beanName) {
        this.clazzName = clazzName;
        this.beanName = beanName;
        this.clazzType = resolveClassName(clazzName);
    }

    private Class<?> resolveClassName(String clazzName) {
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addPropertyValue(PropertyValueOriginal propertyValue) {
        this.propertyValues.add(propertyValue);
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<PropertyValueOriginal> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<PropertyValueOriginal> propertyValues) {
        this.propertyValues = propertyValues;
    }
    public Class<?> getClazzType() {
        return clazzType;
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(this.scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(this.scope);
    }

}
