package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Book;
import com.softkata.softbookstore.domain.CartBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softkata.softbookstore.constants.Constant.MIN_BOOKS_REQUIRED_IN_CART;

@Service
@RequiredArgsConstructor
public class CartValidationService {

    private static final String EMPTY_CART_MESSAGE = "No books are selected to process the cart";

    private final BookStoreService bookStoreService;

    public void validateBeforeProcessingCart(List<CartBook> cartBooks) {

        if(cartBooks == null || cartBooks.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_CART_MESSAGE);
        }

        Set<Integer> validBookIds = this.bookStoreService.getBookMasterData().stream()
                .map(Book::bookId)
                .collect(Collectors.toSet());

        for (CartBook cartbook : cartBooks) {
            if (!validBookIds.contains(cartbook.bookId())) {
                throw new IllegalArgumentException("Validation Failed: Book ID " + cartbook.bookId() + " does not exist in the master catalog.");
            }
        }

        for (CartBook cartbook : cartBooks) {
            if (cartbook.copies() <MIN_BOOKS_REQUIRED_IN_CART) {
                throw new IllegalArgumentException("Validation Failed: Number of copies cannot be less than 1 for book ID " + cartbook.bookId());
            }
        }
    }
}
