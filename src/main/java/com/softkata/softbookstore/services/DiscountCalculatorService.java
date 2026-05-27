package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Book;
import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.DiscountData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public double processDiscount(List<CartBook> cartBooks) {

        validateBeforeProcessingDiscount(cartBooks);

        int totalBooksInCart = 0;
        int distinctBookCnt = cartBooks.size();
        double maxDiscount = 0;

        //Get total number of books in the cart
        for (CartBook book: cartBooks) {
            totalBooksInCart += book.copies();
        }

        //This map is created to have copy of book list and used to distribute each book copy under different sets
        Map<Integer, Integer> bookMainCopiesMap =
                cartBooks.stream()
                        .collect(Collectors.toMap(
                                CartBook::bookId,
                                CartBook::copies
                        ));


        //This will look for various possibilities to get best discount amount
        //If only one distinct book present in the list then it will straight go to return statement with 0 discount
        for (int dProb = distinctBookCnt; dProb >1; dProb--) {
            int tbcCnt = totalBooksInCart;
            Map<Integer, Integer> bookCopiesMap = new HashMap<>(bookMainCopiesMap);
            List<Set<Integer>> bookDiscList = new ArrayList<>();
            while (tbcCnt >0) {
                Set<Integer> bookSet = new HashSet<>();
                for (CartBook book: cartBooks) {
                    if (bookCopiesMap.get(book.bookId()) >0) {
                        bookSet.add(book.bookId());
                        bookCopiesMap.put(book.bookId(), bookCopiesMap.get(book.bookId()) -1);
                        tbcCnt--;
                        if(bookSet.size() == dProb) {
                            break;
                        }
                    }
                }
                bookDiscList.add(bookSet);
            }

            //process set with standard discount rate
            double tempTotalDisc = 0;
            for(Set<Integer> bookset: bookDiscList) {
                int elgDiscRate = getDiscount(bookset.size());
                tempTotalDisc = tempTotalDisc + (elgDiscRate/100.0) * bookset.size() * 50;
            }
            if (tempTotalDisc > maxDiscount) {
                maxDiscount = tempTotalDisc;
            }
        }
        BigDecimal bd = new BigDecimal(Double.toString(maxDiscount));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();


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
