package com.example.stockapp.exceptions;

public class InsufficientQuantityException extends RuntimeException {

    public InsufficientQuantityException(String message) {
        super(message);
    }
}
