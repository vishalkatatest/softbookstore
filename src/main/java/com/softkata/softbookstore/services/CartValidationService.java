package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Book;
import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.exception.InvalidBookException;
import com.softkata.softbookstore.exception.NegativeCopiesException;
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

        for (CartBook cartBook : cartBooks) {
            validateBookExists(cartBook, validBookIds);
            validateBookCopies(cartBook);
        }
    }


    private void validateBookExists(
            CartBook cartBook,
            Set<Integer> validBookIds) {

        if (!validBookIds.contains(cartBook.bookId())) {
            throw new InvalidBookException(
                    "Validation Failed: Book ID "
                            + cartBook.bookId()
                            + " does not exist in the master catalog."
            );
        }
    }

    private void validateBookCopies(CartBook cartBook) {

        if (cartBook.copies() < MIN_BOOKS_REQUIRED_IN_CART) {
            throw new NegativeCopiesException(
                    "Validation Failed: Number of copies cannot be less than 1 for book ID "
                            + cartBook.bookId()
            );
        }
    }
}
