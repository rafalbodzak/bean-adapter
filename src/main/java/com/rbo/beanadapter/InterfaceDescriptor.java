package com.rbo.beanadapter;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: rbo, Date: 16.12.14, Time: 23:14
 */

@Data
@EqualsAndHashCode(of = "interfaceClass")
public final class InterfaceDescriptor {

    private Class interfaceClass;

    private InterfaceDescriptor(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public static List<InterfaceDescriptor> getCommonInterfaceDescriptors(Class sourceClass, Class destClass){
        List<InterfaceDescriptor> sourceInterfaces = getDeepInterfaces(sourceClass);
        List<InterfaceDescriptor> destInterfaces = getDeepInterfaces(destClass);
        sourceInterfaces.retainAll(destInterfaces);
        return sourceInterfaces;
    }

    private static List<InterfaceDescriptor> getDeepInterfaces(Class clazz){
        List<InterfaceDescriptor> allInterfaces = new ArrayList<InterfaceDescriptor>();
        allInterfaces.addAll(convertToDescriptors(clazz.getInterfaces()));
        for (Class interfaceClass : clazz.getInterfaces()){
            allInterfaces.addAll(convertToDescriptors(interfaceClass.getInterfaces()));
        }
        return allInterfaces;
    }

    private static List<InterfaceDescriptor> convertToDescriptors(Class... classes){
        List<InterfaceDescriptor> interfaceDescriptors = new ArrayList<InterfaceDescriptor>();
        for (Class clazz : classes){
            interfaceDescriptors.add(new InterfaceDescriptor(clazz));
        }
        return interfaceDescriptors;
    }

    public Boolean containsGetter(BeanPropertyDescriptor propertyDescriptor){
        Method getter = propertyDescriptor.getReadMethod();
        Boolean contains = interfaceContainsMethodSignature(interfaceClass, getter);
        if (! contains){
            contains = interfaceDeepContainsMethodSignature(interfaceClass, getter);
        }
        return contains;
    }

    private Boolean interfaceContainsMethodSignature(Class interf, Method getter) {
        for (Method interfaceMethod : interf.getDeclaredMethods()){
            if (methodsSignaturesEquals(getter, interfaceMethod)){
                return true;
            }
        }
        return false;
    }

    private Boolean interfaceDeepContainsMethodSignature(Class interf, Method getter) {
        for (Class subInterface : interf.getInterfaces()){
            if (interfaceContainsMethodSignature(subInterface, getter)){
                return true;
            }
        }
        return false;
    }

    private boolean methodsSignaturesEquals(Method methodA, Method methodB) {
        if (methodA == null || methodB == null){
            return false;
        }
        return methodsNamesEquals(methodA, methodB) && methodsParametersEquals(methodA, methodB);
    }

    private Boolean methodsNamesEquals(Method methodA, Method methodB){
        return methodA.getName().equals(methodB.getName());
    }

    private Boolean methodsParametersEquals(Method methodA, Method methodB){
        List parameterTypesA = Arrays.asList(methodA.getParameterTypes());
        List parameterTypesB = Arrays.asList(methodB.getParameterTypes());
        return parameterTypesA.containsAll(parameterTypesB);
    }
}
