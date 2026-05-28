package com.softkata.softbookstore.exception;

public class NegativeCopiesException extends IllegalArgumentException {
    public NegativeCopiesException(String errorMessage) {
        super(errorMessage);
    }
}
