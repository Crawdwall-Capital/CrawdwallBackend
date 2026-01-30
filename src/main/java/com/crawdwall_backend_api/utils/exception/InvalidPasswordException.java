package com.crawdwall_backend_api.utils.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String message){
        super(message);
    }
}
