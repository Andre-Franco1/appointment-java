package com.abutua.appointment.domain.services.exceptions;

public class BusinessException extends RuntimeException {
    
    public BusinessException(String message){
        super(message);
    }
    
}