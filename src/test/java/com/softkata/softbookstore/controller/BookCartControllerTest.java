package com.softkata.softbookstore.controller;


import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookCartControllerTest {

    @Autowired
    BookCartController bookCartController;

    @Test
    public void shouldReturnFinalAmountWithDiscount() {

        CartBook cartBook1 = new CartBook(1001, "Clean Code", 1);
        CartBook cartBook2 = new CartBook(1002, "The Clean Coder", 1);
        CartBook cartBook3 = new CartBook(1003, "Clean Architecture", 1);
        List<CartBook> books = List.of(cartBook1, cartBook2, cartBook3);
        Cart bookCart = new Cart(books);

        ResponseEntity<String> response =
                bookCartController.processCart(bookCart);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("135.0", response.getBody());
    }

}
