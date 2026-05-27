package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookCartServiceTests {

    @Autowired
    BookCartService bookCartService;


    @Test
    public void checkForDiscountProcessing() {
        CartBook cartBook1 = new CartBook(1001, "Clean Code", 1,50);
        CartBook cartBook2 = new CartBook(1002, "The Clean Coder", 1,50);
        CartBook cartBook3 = new CartBook(1003, "Clean Architecture", 1,50);
        List<CartBook> books = List.of(cartBook1, cartBook2, cartBook3);
        Cart bookCart = new Cart(books);
        double totalCartAmt = this.bookCartService.processCartAmount(bookCart);
        assertEquals(135, totalCartAmt);
    }
}
