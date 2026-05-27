package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Book;
import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.DiscountProperties;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class DiscountCalculatorService {

    DiscountProperties discountProperties;
    BookStoreService bookStoreService;
    public DiscountCalculatorService(DiscountProperties discountProperties, BookStoreService bookStoreService) {
        this.discountProperties = discountProperties;
        this.bookStoreService = bookStoreService;
    }

    public int getDiscount(int totalBooks) {

        return  switch (totalBooks) {
            case 2 -> discountProperties.getDiscountFor2Books();
            case 3 -> discountProperties.getDiscountFor3Books();
            case 4 -> discountProperties.getDiscountFor4Books();
            case 5 -> discountProperties.getDiscountFor5Books();
            default -> 0;
        };

    }

    public void processDiscount(List<CartBook> cartBooks) {

        validateBeforeProcessingDiscount(cartBooks);

    }

    private void validateBeforeProcessingDiscount(List<CartBook> cartBooks) {

        //Check if list of books are not empty
        if(cartBooks == null || cartBooks.isEmpty()) {
            throw new IllegalArgumentException("No books are selected to process the cart");
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
    }

}
