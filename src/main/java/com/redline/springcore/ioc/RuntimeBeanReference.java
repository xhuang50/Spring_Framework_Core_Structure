package com.redline.springcore.ioc;

public class RuntimeBeanReference {
    // reference property value
    private String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public RuntimeBeanReference(String ref) {
        super();
        this.ref = ref;
    }

}
