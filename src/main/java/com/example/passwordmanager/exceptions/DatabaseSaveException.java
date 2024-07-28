package com.example.passwordmanager.exceptions;

public class DatabaseSaveException extends RuntimeException{

    public DatabaseSaveException(String message,Throwable throwable) {
        super(message,throwable);
    }
}
