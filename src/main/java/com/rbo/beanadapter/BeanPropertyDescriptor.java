package com.rbo.beanadapter;

import com.rbo.beanadapter.exception.InterfaceBeanAdapterException;
import com.rbo.beanadapter.exception.InterfaceBeanAdapterExceptionCause;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * User: rbo, Date: 16.12.14, Time: 22:36
 */
public class BeanPropertyDescriptor {

    private PropertyDescriptor propertyDescriptor;

    private Object bean;

    public BeanPropertyDescriptor(Object bean, PropertyDescriptor propertyDescriptor) {
        this.bean = bean;
        this.propertyDescriptor = propertyDescriptor;
    }

    public Object getPropertyValue() {
        try {
            return propertyDescriptor.getReadMethod().invoke(bean);
        } catch (Exception e) {
            throw new InterfaceBeanAdapterException(InterfaceBeanAdapterExceptionCause.PROPERTY_GETTER_INVOKE_EXCEPTION, e);
        }
    }

    public void setPropertyValue(Object value) {
        try {
            propertyDescriptor.getWriteMethod().invoke(bean, value);
        } catch (Exception e) {
            throw new InterfaceBeanAdapterException(InterfaceBeanAdapterExceptionCause.PROPERTY_SETTER_INVOKE_EXCEPTION, e);
        }
    }

    public Method getReadMethod(){
        return propertyDescriptor.getReadMethod();
    }

    public String getName(){
        return propertyDescriptor.getName();
    }

    public Class<?> getPropertyType(){
        return propertyDescriptor.getPropertyType();
    }
}
