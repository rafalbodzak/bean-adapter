package com.rbo.beanadapter;

import com.rbo.beanadapter.exception.InterfaceBeanAdapterException;
import com.rbo.beanadapter.exception.InterfaceBeanAdapterExceptionCause;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * User: rbo, Date: 16.12.14, Time: 22:20
 */
public class BeanPropertyInfo {

    private BeanInfo beanInfo;

    private Object bean;

    public BeanPropertyInfo(Object bean){
        this.bean = bean;
        initBeanInfo();
    }

    private void initBeanInfo() {
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            throw new InterfaceBeanAdapterException(InterfaceBeanAdapterExceptionCause.UNKNOWN_EXCEPTION, e);
        }
    }

    public List<BeanPropertyDescriptor> getPropertyDescriptors(){
        List<BeanPropertyDescriptor> beanPropertyDescriptors = new ArrayList<BeanPropertyDescriptor>();
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()){
            beanPropertyDescriptors.add(new BeanPropertyDescriptor(bean, propertyDescriptor));
        }
        return beanPropertyDescriptors;
    }

    public BeanPropertyDescriptor getPropertyDescriptorByOther(BeanPropertyDescriptor otherPropertyDescriptor){
        return getPropertyDescriptorByName(otherPropertyDescriptor.getName());
    }

    public BeanPropertyDescriptor getPropertyDescriptorByName(String propertyDescriptorName){
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()){
            if (propertyDescriptor.getName().equals(propertyDescriptorName)){
                return new BeanPropertyDescriptor(bean, propertyDescriptor);
            }
        }
        throw new InterfaceBeanAdapterException(InterfaceBeanAdapterExceptionCause.NO_SUCH_PROPERTY_IN_BEAN_EXCEPTION);
    }

    public Object getPropertyValueByName(String propertyName){
        return getPropertyDescriptorByName(propertyName).getPropertyValue();
    }

}
