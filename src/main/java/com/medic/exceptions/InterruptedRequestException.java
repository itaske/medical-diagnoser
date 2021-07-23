package com.medic.exceptions;

public class InterruptedRequestException extends RuntimeException{
    public InterruptedRequestException(String message) {
        super(message);
    }
}
