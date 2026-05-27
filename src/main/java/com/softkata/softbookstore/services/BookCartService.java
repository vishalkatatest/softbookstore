package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartBook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCartService {
    DiscountCalculatorService discountCalculatorService;
    BookStoreService bookStoreService;

    public BookCartService(DiscountCalculatorService discountCalculatorService, BookStoreService bookStoreService) {
        this.discountCalculatorService = discountCalculatorService;
        this.bookStoreService = bookStoreService;
    }

    public double processCartAmount(Cart bookCart) {
        List<CartBook> books = bookCart.books();
        double discAmt = discountCalculatorService.processDiscount(books);
        return totalAmount(books) - discAmt;
    }

    private double totalAmount(List<CartBook> books) {
        return books.stream().mapToDouble(book -> {
            double unitPrice = bookStoreService.getMasterBookPriceMapCopy().getOrDefault(book.bookId(), 0.0);
            return book.copies() * unitPrice;
        }).sum();
    }
}
