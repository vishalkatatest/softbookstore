package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Book;
import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.DiscountData;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class DiscountCalculatorService {

    DiscountRulesService discountRulesService;
    BookStoreService bookStoreService;

    public DiscountCalculatorService(DiscountRulesService discountRulesService, BookStoreService bookStoreService) {
        this.discountRulesService = discountRulesService;
        this.bookStoreService = bookStoreService;
    }

    public int getDiscount(int totalBooks) {

        List<DiscountData> discountRules = discountRulesService.getDiscountRules();

        if (discountRules == null || discountRules.isEmpty() || totalBooks <= 0) {
            return 0;
        }

        return discountRules.stream()
                .filter(rule -> totalBooks == rule.noOfBooks())
                .mapToInt(DiscountData::discPercent)
                .findFirst()
                .orElse(0);

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
