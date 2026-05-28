package com.softkata.softbookstore.exception;

import com.softkata.softbookstore.domain.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleEmptyCartException() {

        IllegalArgumentException exception =
                new IllegalArgumentException("No books are selected to process the cart");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleEmptyCartException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "EMPTY_CART",
                response.getBody().getMessageCode()
        );

        assertEquals(
                "No books are selected to process the cart",
                response.getBody().getErrorMessage()
        );
    }

    @Test
    void shouldHandleInvalidBookException() {

        InvalidBookException exception =
                new InvalidBookException("Validation Failed: Book ID 1006 does not exist in the master catalog.");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleInvalidBookInCartException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(
                "INVALID_BOOK",
                response.getBody().getMessageCode()
        );

        assertEquals(
                "Validation Failed: Book ID 1006 does not exist in the master catalog.",
                response.getBody().getErrorMessage()
        );
    }

    @Test
    void shouldHandleNegativeCopiesException() {

        NegativeCopiesException exception =
                new NegativeCopiesException(
                        "Validation Failed: Number of copies cannot be less than 1 for book ID 1001"
                );

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleNegativeCopiesInCartException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(
                "INVALID_COPIES",
                response.getBody().getMessageCode()
        );

        assertEquals(
                "Validation Failed: Number of copies cannot be less than 1 for book ID 1001",
                response.getBody().getErrorMessage()
        );
    }

    @Test
    void shouldHandleGenericException() {

        Exception exception =
                new IOException("Cannot read properties file");

        ResponseEntity<ErrorResponse> response =
                globalExceptionHandler.handleAllGenericError(exception);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "UNEXPECTED_ERROR",
                response.getBody().getMessageCode()
        );

        assertEquals(
                "An unexpected error occurred: Cannot read properties file",
                response.getBody().getErrorMessage()
        );
    }
}