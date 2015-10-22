package com.rbo.beanadapter;

import com.rbo.beanadapter.exception.InterfaceBeanAdapterException;
import com.rbo.beanadapter.exception.InterfaceBeanAdapterExceptionCause;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: rbo, Date: 12.12.14, Time: 23:35
 */
public class BeanAdapterIdObjectList {

    private Map<Object, Object> beanAdapterIdValueToObjectMap = new HashMap<Object, Object>();

    public BeanAdapterIdObjectList(List<Object> objectList){
        initBeanAdapterIdMap(objectList);
    }

    private void initBeanAdapterIdMap(List<Object> objectList){
        for (Object object : objectList){
            Object beanAdapterIdValue = getBeanAdapterIdValue(object);
            beanAdapterIdValueToObjectMap.put(beanAdapterIdValue, object);
        }
    }

    public Boolean contains(Object object){
        return beanAdapterIdValueToObjectMap.containsKey(getBeanAdapterIdValue(object));
    }

    public Object get(Object object){
        return beanAdapterIdValueToObjectMap.get(getBeanAdapterIdValue(object));
    }

    private Object getBeanAdapterIdValue(Object object){
        BeanAdapterId beanAdapterIdAnnotation = object.getClass().getAnnotation(BeanAdapterId.class);
        if (beanAdapterIdAnnotation == null){
            throw new InterfaceBeanAdapterException(InterfaceBeanAdapterExceptionCause.BEAN_ADAPTER_ID_ANNOTATION_MISSING);
        }
        String beanAdapterIdPropertyName = beanAdapterIdAnnotation.propertyName();
        BeanPropertyInfo beanPropertyInfo = new BeanPropertyInfo(object);
        return beanPropertyInfo.getPropertyValueByName(beanAdapterIdPropertyName);
    }
}
