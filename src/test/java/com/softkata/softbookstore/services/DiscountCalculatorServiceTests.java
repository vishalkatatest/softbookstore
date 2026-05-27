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
public class DiscountCalculatorServiceTests {

    BookTestDataProvider bookTestDataProvider;

    @BeforeEach
    public void setupData() {
        bookTestDataProvider = new BookTestDataProvider();
    }

    @Autowired
    private DiscountCalculatorService discountCalculatorService;

    @Test
    public void checkDiscountPercentForSingleDistinctBooks() {
        int discVal = this.discountCalculatorService.getDiscount(1);
        assertEquals(0,discVal);
    }

    @Test
    public void checkDiscountPercentForTwoDistinctBooks() {
        int discVal = this.discountCalculatorService.getDiscount(2);
        assertEquals(5,discVal);
    }

    @Test
    public void checkDiscountPercentForThreeDistinctBooks() {
        int discVal = this.discountCalculatorService.getDiscount(3);
        assertEquals(10,discVal);
    }

    @Test
    public void checkDiscountPercentForFourDistinctBooks() {
        int discVal = this.discountCalculatorService.getDiscount(4);
        assertEquals(20,discVal);
    }

    @Test
    public void checkDiscountPercentForFiveDistinctBooks() {
        int discVal = this.discountCalculatorService.getDiscount(5);
        assertEquals(25,discVal);
    }

    @Test()
    public void validateIfNoBooksAreSelectedToProcessDiscount() {
        List<CartBook> books = new ArrayList<>();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> this.discountCalculatorService.processDiscount(books),
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
                () -> this.discountCalculatorService.processDiscount(books),
                "Should throw IllegalArgumentException for Unknown Book ID"
        );
        assertEquals("Validation Failed: Book ID " + cartBook1.bookId() + " does not exist in the master catalog.", exception.getMessage());

    }


    @Test()
    public void validateIfBookCopiesPassedWithNegativeNumber() {
        List<CartBook> books = List.of(bookTestDataProvider.addDummyCodeBook(-2));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> this.discountCalculatorService.processDiscount(books),
                "Should throw IllegalArgumentException for Negative copies"
        );
        assertEquals("Validation Failed: Number of copies cannot be less than 1 for book ID " + books.getFirst().bookId(), exception.getMessage());

    }

    @Test
    public void checkForDiscountCalculationWhen1DistinctBookSelected() {
        List<CartBook> books = List.of(bookTestDataProvider.addDummyCodeBook(2));
        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(0, discountAmt);
    }

    @Test
    public void checkForDiscountCalculationWhen2DistinctBookSelected() {
        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(1),
                bookTestDataProvider.addDummyCoderBook(1)
        );
        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(5, discountAmt);
    }

    @Test
    public void checkForDiscountCalculationWhen3DistinctBookSelected() {
        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(1),
                bookTestDataProvider.addDummyCoderBook(1),
                bookTestDataProvider.addDummyCleanArchitectureBook(1)
        );
        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(15, discountAmt);
    }

    @Test
    public void checkForDiscountCalculationWhen4DistinctBookSelected() {

        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(1),
                bookTestDataProvider.addDummyCoderBook(1),
                bookTestDataProvider.addDummyCleanArchitectureBook(1),
                bookTestDataProvider.addDummyTDDByExampleBook(1)
        );
        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(40, discountAmt);
    }

    @Test
    public void checkForDiscountCalculationWhen5DistinctBookSelected() {

        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(1),
                bookTestDataProvider.addDummyCoderBook(1),
                bookTestDataProvider.addDummyCleanArchitectureBook(1),
                bookTestDataProvider.addDummyTDDByExampleBook(1),
                bookTestDataProvider.addDummyWorkingWithLegacyBook(1)
        );
        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(62.5, discountAmt);
    }

    @Test
    public void checkForMixedBagDiscountCalculation() {

        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(2),
                bookTestDataProvider.addDummyCoderBook(2),
                bookTestDataProvider.addDummyCleanArchitectureBook(2),
                bookTestDataProvider.addDummyTDDByExampleBook(1),
                bookTestDataProvider.addDummyWorkingWithLegacyBook(1)
        );
        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(80, discountAmt);
    }

    @Test
    public void checkForMixedBag2DiscountCalculation() {

        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(2),
                bookTestDataProvider.addDummyCoderBook(2),
                bookTestDataProvider.addDummyCleanArchitectureBook(2),
                bookTestDataProvider.addDummyTDDByExampleBook(2),
                bookTestDataProvider.addDummyWorkingWithLegacyBook(2)
        );
        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(125, discountAmt);
    }

    @Test
    public void checkForMixedBagDiscountWithMultipleEntriesCalculation() {

        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(1),
                bookTestDataProvider.addDummyCodeBook(1),
                bookTestDataProvider.addDummyCoderBook(2),
                bookTestDataProvider.addDummyCleanArchitectureBook(2),
                bookTestDataProvider.addDummyTDDByExampleBook(1),
                bookTestDataProvider.addDummyWorkingWithLegacyBook(1)
        );
        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(80, discountAmt);
    }



}
