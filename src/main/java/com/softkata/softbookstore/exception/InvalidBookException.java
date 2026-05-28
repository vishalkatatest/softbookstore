package com.softkata.softbookstore.exception;

public class InvalidBookException extends IllegalArgumentException {
    public InvalidBookException(String errorMessage) {
        super(errorMessage);
    }
}
