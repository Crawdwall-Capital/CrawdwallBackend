package com.crawdwall_backend_api.utils.exception;

public class ResourceExistsException extends RuntimeException{
    public ResourceExistsException(String message){
        super(message);
    }
}
