package com.crawdwall_backend_api.utils.exception;

public class InvalidOperationException extends RuntimeException{
    public InvalidOperationException(String message){
        super(message);
    }
}
