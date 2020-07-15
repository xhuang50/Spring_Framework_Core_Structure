package com.redline.springcore.ioc;

public class TypedStringValueOriginal {
    private String value;

    private Class<?> targetType;

    public TypedStringValueOriginal(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType;
    }

}
