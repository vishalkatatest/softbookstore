package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCartService {
    private final DiscountCalculatorService discountCalculatorService;
    private final BookStoreService bookStoreService;
    private final CartValidationService cartValidationService;

    public CartResponse processCartAmount(Cart bookCart) {
        try {
            List<CartBook> books = bookCart.books();
            cartValidationService.validateBeforeProcessingCart(books);
            double discAmt = discountCalculatorService.processDiscount(books);
            return CartResponse.builder().discountAmount(discAmt).cartAmount(totalAmount(books) - discAmt).build();

        } catch (Exception e) {
            return CartResponse.builder().errorMessage(e.getMessage()).build();
        }
    }

    private double totalAmount(List<CartBook> books) {
        return books.stream().mapToDouble(book -> {
            double unitPrice = bookStoreService.getMasterBookPriceMapCopy().getOrDefault(book.bookId(), 0.0);
            return book.copies() * unitPrice;
        }).sum();
    }
}
