package com.redline.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {
    public static Object invoke(Class clazz,String methodName,Object bean) throws Exception{
        Method method = clazz.getDeclaredMethod(methodName);
        // 通过反射调用初始化方法
        return method.invoke(bean);
    }

    public static void setPropertyValue(Object bean, String name, Object value,Class clazz) {
        // TODO spring中的依赖注入到底是通过属性注入还是setter方法注入
        try{
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean,value);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static Object newInstance(Class clazz) {
        try {
            // 获取无参构造器
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object bean = constructor.newInstance();
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getTypeByFieldName(String beanClassName, String name) {
        try {
            Class<?> clazz = Class.forName(beanClassName);
            Field field = clazz.getDeclaredField(name);
            return field.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}