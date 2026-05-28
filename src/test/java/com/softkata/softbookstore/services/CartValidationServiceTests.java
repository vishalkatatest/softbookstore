package com.softkata.softbookstore.services;

import com.softkata.softbookstore.BookTestDataProvider;
import com.softkata.softbookstore.domain.CartBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CartValidationServiceTests {

    @Autowired
    CartValidationService cartValidationService;

    BookTestDataProvider bookTestDataProvider;

    @BeforeEach
    public void setupData() {
        bookTestDataProvider = new BookTestDataProvider();
    }

    @Test()
    public void validateIfNoBooksAreSelectedToProcessDiscount() {
        List<CartBook> books = new ArrayList<>();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> this.cartValidationService.validateBeforeProcessingCart(books),
                "Should throw IllegalArgumentException for empty book list"
        );
        assertEquals("No books are selected to process the cart", exception.getMessage());

    }

    @Test()
    public void validateIfUnknownBoookIDPassedToProcessDiscount() {
        CartBook cartBook1 = new CartBook(1006, "The Coder", 2);
        List<CartBook> books = new ArrayList<>();
        books.add(cartBook1);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> this.cartValidationService.validateBeforeProcessingCart(books),
                "Should throw IllegalArgumentException for Unknown Book ID"
        );
        assertEquals("Validation Failed: Book ID " + cartBook1.bookId() + " does not exist in the master catalog.", exception.getMessage());

    }


    @Test()
    public void validateIfBookCopiesPassedWithNegativeNumber() {
        List<CartBook> books = List.of(bookTestDataProvider.addDummyCodeBook(-2));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartValidationService.validateBeforeProcessingCart(books),
                "Should throw IllegalArgumentException for Negative copies"
        );
        assertEquals("Validation Failed: Number of copies cannot be less than 1 for book ID " + books.getFirst().bookId(), exception.getMessage());

    }
}
