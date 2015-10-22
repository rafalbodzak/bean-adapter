package com.rbo.beanadapter.exception;

/**
 * User: rbo, Date: 06.07.14, Time: 09:05
 */
public enum InterfaceBeanAdapterExceptionCause {

    UNKNOWN_EXCEPTION(1, "Unknown exception."),
    DESTINATION_BEAN_CREATION_EXCEPTION(2, "Cannot create destination bean by reflection. Check provided class constructors."),
    NO_SUCH_PROPERTY_IN_BEAN_EXCEPTION(3, "No such property in bean."),
    PROPERTY_SETTER_INVOKE_EXCEPTION(4, "Exception when trying to adapt value by destination bean."),
    PROPERTY_GETTER_INVOKE_EXCEPTION(5, "Exception when trying to get value from source bean."),
    BEAN_ADAPTER_ID_ANNOTATION_MISSING(6, "com.rbo.beanadapter.BeanAdapterId annotation missing in object."),
    ;
    public static final String INTERFACE_BEAN_ADAPTER_CODE = "IBA-";

    private String message;
    private Integer id;

    private InterfaceBeanAdapterExceptionCause(Integer id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getFullName(){
        return new StringBuilder(INTERFACE_BEAN_ADAPTER_CODE).append(id).append(". ").append(message).toString();
    }
}
