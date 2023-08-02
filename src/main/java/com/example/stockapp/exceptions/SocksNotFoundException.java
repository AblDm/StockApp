package com.example.stockapp.exceptions;

public class SocksNotFoundException extends RuntimeException {

    public SocksNotFoundException(String message) {
        super(message);
    }
}