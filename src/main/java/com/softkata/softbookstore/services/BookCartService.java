package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartBook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCartService {
    DiscountCalculatorService discountCalculatorService;

    public BookCartService(DiscountCalculatorService discountCalculatorService) {
        this.discountCalculatorService = discountCalculatorService;
    }

    public double processCartAmount(Cart bookCart) {
        List<CartBook> books = bookCart.books();
        double discAmt = discountCalculatorService.processDiscount(books);
        return totalAmount(books) - discAmt;
    }

    private double totalAmount(List<CartBook> books) {
        return books.stream().mapToDouble(book -> book.copies()* book.price()).sum();
    }
}
