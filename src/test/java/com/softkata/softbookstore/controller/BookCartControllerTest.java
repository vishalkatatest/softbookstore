package com.softkata.softbookstore.controller;


import com.softkata.softbookstore.BookTestDataProvider;
import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookCartControllerTest {

    BookTestDataProvider bookTestDataProvider;

    @BeforeEach
    public void setupData() {
        bookTestDataProvider = new BookTestDataProvider();
    }

    @Autowired
    BookCartController bookCartController;

    @Test
    public void shouldReturnFinalAmountWithDiscount() {

        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(2),
                bookTestDataProvider.addDummyCoderBook(2),
                bookTestDataProvider.addDummyCleanArchitectureBook(2),
                bookTestDataProvider.addDummyTDDByExampleBook(1),
                bookTestDataProvider.addDummyWorkingWithLegacyBook(1));
        Cart bookCart = new Cart(books);

        ResponseEntity<String> response =
                bookCartController.processCart(bookCart);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("320.0", response.getBody());
    }

}
