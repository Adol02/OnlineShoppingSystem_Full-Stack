package com.example.exceptions;
public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(String message) {
        super(message);
    }
}
