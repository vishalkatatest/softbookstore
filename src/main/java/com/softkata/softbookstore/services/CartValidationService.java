package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Book;
import com.softkata.softbookstore.domain.CartBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartValidationService {

    private static final String EMPTY_CART_MESSAGE = "No books are selected to process the cart";

    private final BookStoreService bookStoreService;

    public void validateBeforeProcessingCart(List<CartBook> cartBooks) {

        //Check if list of books are not empty
        if(cartBooks == null || cartBooks.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_CART_MESSAGE);
        }

        //Retrieve all valid book IDs from master data
        Set<Integer> validBookIds = this.bookStoreService.getBookMasterData().stream()
                .map(Book::bookId)
                .collect(Collectors.toSet());

        // Check for any unknown IDs
        for (CartBook cartbook : cartBooks) {
            if (!validBookIds.contains(cartbook.bookId())) {
                throw new IllegalArgumentException("Validation Failed: Book ID " + cartbook.bookId() + " does not exist in the master catalog.");
            }
        }

        // Check for any book copy with negative numbers
        for (CartBook cartbook : cartBooks) {
            if (cartbook.copies() <1) {
                throw new IllegalArgumentException("Validation Failed: Number of copies cannot be less than 1 for book ID " + cartbook.bookId());
            }
        }
    }
}
