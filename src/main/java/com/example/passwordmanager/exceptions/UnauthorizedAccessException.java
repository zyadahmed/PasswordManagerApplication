package com.example.passwordmanager.exceptions;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
