package com.softkata.softbookstore.services;

import com.softkata.softbookstore.BookTestDataProvider;
import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.CartResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookCartServiceTests {

    BookTestDataProvider bookTestDataProvider;

    @Autowired
    BookCartService bookCartService;

    @BeforeEach
    public void setupData() {
        bookTestDataProvider = new BookTestDataProvider();
    }

    @Test
    public void checkForDiscountProcessing() {
        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(1),
                bookTestDataProvider.addDummyCoderBook(1),
                bookTestDataProvider.addDummyCleanArchitectureBook(1)
        );
        Cart bookCart = new Cart(books);
        CartResponse cartResponse = this.bookCartService.processCartAmount(bookCart);
        assertEquals(135, cartResponse.getCartAmount());
    }

    @Test
    public void processBookCartAmount() {

        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(2),
                bookTestDataProvider.addDummyCoderBook(2),
                bookTestDataProvider.addDummyCleanArchitectureBook(2),
                bookTestDataProvider.addDummyTDDByExampleBook(1),
                bookTestDataProvider.addDummyWorkingWithLegacyBook(1)
        );
        Cart bookCart = new Cart(books);
        CartResponse cartResponse = this.bookCartService.processCartAmount(bookCart);
        assertEquals(320, cartResponse.getCartAmount());
        assertEquals(80, cartResponse.getDiscountAmount());

    }

    @Test()
    public void validateIfNoBooksAreSelectedToServiceProcessDiscount() {
        List<CartBook> books = new ArrayList<>();
        Cart bookCart = new Cart(books);
        CartResponse cartResponse = this.bookCartService.processCartAmount(bookCart);
        assertEquals("No books are selected to process the cart", cartResponse.getErrorMessage());

    }
}
