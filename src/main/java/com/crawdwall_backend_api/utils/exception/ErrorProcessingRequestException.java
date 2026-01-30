package com.crawdwall_backend_api.utils.exception;

public class ErrorProcessingRequestException extends RuntimeException{

    public ErrorProcessingRequestException(String message){
        super(message);
    }
}
