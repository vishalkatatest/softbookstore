package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.softkata.softbookstore.constants.Constant.ZERO_DOUBLE;

@Service
@RequiredArgsConstructor
public class BookCartService {
    private final DiscountCalculatorService discountCalculatorService;
    private final BookStoreService bookStoreService;
    private final CartValidationService cartValidationService;

    public CartResponse processCartAmount(Cart bookCart) {
            List<CartBook> books = bookCart.books();
            cartValidationService.validateBeforeProcessingCart(books);
            double discAmt = discountCalculatorService.processDiscount(books);
            return CartResponse.builder().discountAmount(discAmt).cartAmount(totalAmount(books) - discAmt).build();
    }

    private double totalAmount(List<CartBook> books) {
        return books.stream().mapToDouble(book -> {
            double unitPrice = bookStoreService.getMasterBookPriceMapCopy().getOrDefault(book.bookId(), ZERO_DOUBLE);
            return book.copies() * unitPrice;
        }).sum();
    }
}
