package com.abutua.appointment.domain.services.exceptions;

public class DatabaseException extends RuntimeException {
    
    public DatabaseException(String message){
        super(message);
    }
}