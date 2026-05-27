package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.CartBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class DiscountCalculatorServiceTests {

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

    @Test
    public void checkForDiscountCalculationWhen1DistinctBookSelected() {
        CartBook cartBook1 = new CartBook(1001, "Clean Code", 1);
        List<CartBook> books = List.of(cartBook1);
        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(0, discountAmt);
    }

    @Test
    public void checkForDiscountCalculationWhen2DistinctBookSelected() {
        CartBook cartBook1 = new CartBook(1001, "Clean Code", 1);
        CartBook cartBook2 = new CartBook(1002, "The Clean Coder", 1);
        List<CartBook> books = List.of(cartBook1, cartBook2);

        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(5, discountAmt);
    }

    @Test
    public void checkForDiscountCalculationWhen3DistinctBookSelected() {

        CartBook cartBook1 = new CartBook(1001, "Clean Code", 1);
        CartBook cartBook2 = new CartBook(1002, "The Clean Coder", 1);
        CartBook cartBook3 = new CartBook(1003, "Clean Architecture", 1);
        List<CartBook> books = List.of(cartBook1, cartBook2, cartBook3);

        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(15, discountAmt);
    }

    @Test
    public void checkForDiscountCalculationWhen4DistinctBookSelected() {

        CartBook cartBook1 = new CartBook(1001, "Clean Code", 1);
        CartBook cartBook2 = new CartBook(1002, "The Clean Coder", 1);
        CartBook cartBook3 = new CartBook(1003, "Clean Architecture", 1);
        CartBook cartBook4 = new CartBook(1004, "Test Driven Development by Example", 1);
        List<CartBook> books = List.of(cartBook1, cartBook2, cartBook3, cartBook4);

        double discountAmt = this.discountCalculatorService.processDiscount(books);
        assertEquals(40, discountAmt);
    }


}
