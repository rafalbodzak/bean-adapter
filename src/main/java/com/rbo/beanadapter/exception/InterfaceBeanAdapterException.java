package com.rbo.beanadapter.exception;

/**
 * User: rbo, Date: 06.07.14, Time: 09:03
 */
public class InterfaceBeanAdapterException extends RuntimeException {

    public InterfaceBeanAdapterException(InterfaceBeanAdapterExceptionCause ibaCause, Throwable cause) {
        super(ibaCause.getFullName(), cause);
    }

    public InterfaceBeanAdapterException(InterfaceBeanAdapterExceptionCause ibaCause){
        super(ibaCause.getFullName());
    }
}
