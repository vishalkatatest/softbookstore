package com.softkata.softbookstore.services;

import com.softkata.softbookstore.BookTestDataProvider;
import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.CartResponse;
import com.softkata.softbookstore.exception.InvalidBookException;
import com.softkata.softbookstore.exception.NegativeCopiesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void validateIfNoBooksAreSelectedToServiceProcessCart() {
        List<CartBook> books = new ArrayList<>();
        Cart bookCart = new Cart(books);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> this.bookCartService.processCartAmount(bookCart),
                "Should throw IllegalArgumentException for empty book list"
        );
        assertEquals("No books are selected to process the cart", exception.getMessage());

    }

    @Test()
    public void validateIfBookCopiesPassedWithNullList() {
        List<CartBook> books = null;
        Cart bookCart = new Cart(books);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> this.bookCartService.processCartAmount(bookCart),
                "Should throw IllegalArgumentException for empty book list"
        );
        assertEquals("No books are selected to process the cart", exception.getMessage());

    }


    @Test()
    public void validateIfUnknownBoookIDPassedToProcessCart() {
        CartBook cartBook1 = new CartBook(1006, "The Coder", 2);
        List<CartBook> books = new ArrayList<>();
        books.add(cartBook1);
        Cart bookCart = new Cart(books);
        InvalidBookException exception = assertThrows(
                InvalidBookException.class,
                () -> this.bookCartService.processCartAmount(bookCart),
                "Should throw InvalidBookException for Unknown Book ID"
        );
        assertEquals("Validation Failed: Book ID " + books.getFirst().bookId() + " does not exist in the master catalog.", exception.getMessage());

    }


    @Test()
    public void validateIfBookCopiesPassedWithNegativeNumber() {
        List<CartBook> books = List.of(bookTestDataProvider.addDummyCodeBook(-2));
        Cart bookCart = new Cart(books);

        NegativeCopiesException exception = assertThrows(
                NegativeCopiesException.class,
                () -> bookCartService.processCartAmount(bookCart),
                "Should throw NegativeCopiesException for Negative copies"
        );
        assertEquals("Validation Failed: Number of copies cannot be less than 1 for book ID " + books.getFirst().bookId(), exception.getMessage());

    }
}
