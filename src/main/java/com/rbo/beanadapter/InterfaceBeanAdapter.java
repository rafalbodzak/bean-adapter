package com.rbo.beanadapter;

import com.rbo.beanadapter.exception.InterfaceBeanAdapterException;
import com.rbo.beanadapter.exception.InterfaceBeanAdapterExceptionCause;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * User: rbo, Date: 04.07.14, Time: 19:30
 */
@Component
public class InterfaceBeanAdapter implements BeanAdapter{

    public static final String JAVA_LANG = "java.lang";

    public void adapt(Object source, Object dest){
        List<InterfaceDescriptor> commonInterfaces = InterfaceDescriptor.getCommonInterfaceDescriptors(source.getClass(), dest.getClass());
        for (InterfaceDescriptor commonInterface : commonInterfaces){
            adaptByInterface(source, dest, commonInterface);
        }
    }

    public <SOURCE, DEST> List<DEST> adaptToNewList(List<SOURCE> sourceList, Class<DEST> destClass){
        List<DEST> destinationList = new ArrayList<DEST>();
        for (SOURCE source : sourceList){
            DEST newDest = (DEST)getNewObjectInstance(destClass);
            adapt(source, newDest);
            destinationList.add(newDest);
        }
        return destinationList;
    }

    private void adaptByInterface(Object source, Object dest, InterfaceDescriptor interf){
        BeanPropertyInfo sourceBeanInfo = new BeanPropertyInfo(source);
        BeanPropertyInfo destBeanInfo = new BeanPropertyInfo(dest);
        for (BeanPropertyDescriptor propertyDescriptor : sourceBeanInfo.getPropertyDescriptors()){
            adaptPropertyByInterface(interf, destBeanInfo, propertyDescriptor);
        }
    }

    private void adaptPropertyByInterface(InterfaceDescriptor interfaceDescriptor, BeanPropertyInfo destBeanInfo, BeanPropertyDescriptor sourcePropertyDescriptor) {
        if (interfaceDescriptor.containsGetter(sourcePropertyDescriptor)){
            adaptProperty(destBeanInfo, sourcePropertyDescriptor);
        }
    }

    private void adaptProperty(BeanPropertyInfo destBeanInfo, BeanPropertyDescriptor sourcePropertyDescriptor) {
        BeanPropertyDescriptor destPropertyDescriptor = destBeanInfo.getPropertyDescriptorByOther(sourcePropertyDescriptor);
        Object value = getDeepValueFromPropertyDescriptor(sourcePropertyDescriptor, destPropertyDescriptor);
        destPropertyDescriptor.setPropertyValue(value);
    }

    private Object getDeepValueFromPropertyDescriptor(BeanPropertyDescriptor sourcePropertyDescriptor, BeanPropertyDescriptor destPropertyDescriptor) {
        Object sourceValue = sourcePropertyDescriptor.getPropertyValue();
        Object destValue = destPropertyDescriptor.getPropertyValue();
        return getDeepValue(destPropertyDescriptor, sourceValue, destValue);
    }

    private Object getDeepValue(BeanPropertyDescriptor destPropertyDescriptor, Object sourceValue, Object destValue) {
        if (sourceValue == null){
            return sourceValue;
        }
        if (sourceValue instanceof List){
            return adaptListProperty((List) sourceValue, (List)destValue, destPropertyDescriptor);
        }
        if ( ! isTypeBasic(sourceValue.getClass())){
            return adaptProperty(sourceValue, destValue, destPropertyDescriptor.getPropertyType());
        }
        return sourceValue;
    }

    private Object adaptProperty(Object source, Object destParam, Class propertyClass)  {
        Object dest = (destParam != null)? destParam : getNewObjectInstance(propertyClass);
        adapt(source, dest);
        return dest;
    }

    private Object adaptListProperty(List source, List dest, BeanPropertyDescriptor destPropertyDescriptor) {
        Class destClass = getParametrizedTypeIfExists(destPropertyDescriptor);
        if (isTypeBasic(destClass)){
            return adaptBasicListProperty(source);
        }
        return adaptDeepListProperty(source, dest, destClass);
    }

    private List adaptBasicListProperty(List source) {
        return new ArrayList(source);
    }

    private Object adaptDeepListProperty(List sourceList, List destListParam, Class destClass) {
        List destList = (destListParam != null) ? destListParam : new ArrayList();
        BeanAdapterIdObjectList beanAdapterIdObjectList = new BeanAdapterIdObjectList(destList);
        for (int i = 0; i < sourceList.size(); i++){
            Object sourceObj = sourceList.get(i);
            Boolean objectExistsInDestList = beanAdapterIdObjectList.contains(sourceObj);
            Object destObject;
            if (objectExistsInDestList){
                destObject = beanAdapterIdObjectList.get(sourceObj);
            }else{
                destObject = getNewObjectInstance(destClass);
                destList.add(destObject);
            }
            adapt(sourceObj, destObject);
        }
        return destList;
    }

    private Object getNewObjectInstance(Class destClass) {
        try {
            return destClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new InterfaceBeanAdapterException(InterfaceBeanAdapterExceptionCause.DESTINATION_BEAN_CREATION_EXCEPTION, e);
        }
    }

    private Class getParametrizedTypeIfExists(BeanPropertyDescriptor propertyDescriptor){
        Method method = propertyDescriptor.getReadMethod();
        ParameterizedType genericReturnType = (ParameterizedType)method.getGenericReturnType();
        return  (Class)genericReturnType.getActualTypeArguments()[0];
    }

    private Boolean isTypeBasic(Class objectClass){
        return objectClass.isPrimitive() || objectClass.getName().startsWith(JAVA_LANG);
    }

}
