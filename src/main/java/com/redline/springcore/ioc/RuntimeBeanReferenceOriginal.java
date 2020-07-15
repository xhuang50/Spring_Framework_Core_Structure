package com.redline.springcore.ioc;

public class RuntimeBeanReferenceOriginal {
    // reference property value
    private String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public RuntimeBeanReferenceOriginal(String ref) {
        super();
        this.ref = ref;
    }

}
