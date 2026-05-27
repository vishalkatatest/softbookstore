package com.softkata.softbookstore.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
