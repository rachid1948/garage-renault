package com.renault.garage.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String message)
    {
        super(message);
    }
}
