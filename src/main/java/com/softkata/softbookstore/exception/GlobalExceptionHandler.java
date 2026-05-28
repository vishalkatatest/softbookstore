package com.softkata.softbookstore.exception;

import com.softkata.softbookstore.domain.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleEmptyCartException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse("EMPTY_CART", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBookInCartException(InvalidBookException ex) {
        ErrorResponse errorResponse = new ErrorResponse("INVALID_BOOK", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NegativeCopiesException.class)
    public ResponseEntity<ErrorResponse> handleNegativeCopiesInCartException(NegativeCopiesException ex) {
        ErrorResponse errorResponse = new ErrorResponse("INVALID_COPIES", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllGenericError(Exception ex) {

        ErrorResponse error = new ErrorResponse("UNEXPECTED_ERROR", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
