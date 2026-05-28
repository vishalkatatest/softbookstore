package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.DiscountData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DiscountCalculatorService {

    private static final int DECIMAL_ROUND_OFF = 2;
    private static final double TOTAL_PERCENT = 100.0;

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

    public double processDiscount(List<CartBook> cartBookData) {

        List<CartBook> cartBooks = consolidateCart(cartBookData);
        int distinctBookCnt = cartBooks.size();
        double maxDiscount = 0;

        //Get total number of books in the cart
        int totalBooksInCart = cartBooks.stream().mapToInt(CartBook::copies).sum();

        //This map is created to have copy of book list and used to distribute each book copy under different sets
        Map<Integer, Integer> initialCopiesMap = getCartBookCopyMap(cartBooks);


        //This will look for various possibilities to get best discount amount
        //If only one distinct book present in the list then it will straight go to return statement with 0 discount
        for (int distBookProb = distinctBookCnt; distBookProb >1; distBookProb--) {

            Map<Integer, Integer> bookCopiesMap = HashMap.newHashMap(initialCopiesMap.size());
            bookCopiesMap.putAll(initialCopiesMap);

            //Prepare Various Combination Group Set
            List<Set<Integer>> bookDiscList =
                    buildBookSets(distBookProb, totalBooksInCart, bookCopiesMap, cartBooks);

            //process set with standard discount rate
            maxDiscount = processMaxDiscount(bookDiscList, maxDiscount);
        }
        BigDecimal bd = new BigDecimal(Double.toString(maxDiscount));
        bd = bd.setScale(DECIMAL_ROUND_OFF, RoundingMode.HALF_UP);
        return bd.doubleValue();

    }


    private List<Set<Integer>> buildBookSets(
            int groupSize,
            int remainingBooks,
            Map<Integer, Integer> copiesMap,
            List<CartBook> cartBooks) {

        if (remainingBooks <= 0) {
            return Collections.emptyList();
        }

        Set<Integer> currentBookSet = cartBooks.stream()
                .filter(book -> copiesMap.getOrDefault(book.bookId(), 0) > 0)
                .limit(groupSize)
                .map(CartBook::bookId)
                .collect(Collectors.toSet());

        currentBookSet.forEach(bookId ->
                copiesMap.computeIfPresent(bookId, (k, v) -> v - 1));

        int consumed = currentBookSet.size();

        List<Set<Integer>> remainingBookSets =
                buildBookSets(
                        groupSize,
                        remainingBooks - consumed,
                        copiesMap,
                        cartBooks
                );

        List<Set<Integer>> result = new ArrayList<>();
        result.add(currentBookSet);
        result.addAll(remainingBookSets);

        return result;
    }


    private Map<Integer, Integer> getCartBookCopyMap(List<CartBook> cartBooks) {
        return cartBooks.stream()
                .collect(Collectors.toMap(
                        CartBook::bookId,
                        CartBook::copies
                ));
    }


    private double processMaxDiscount(List<Set<Integer>> bookDiscList, double maxDiscount) {

        //get Book Master data map for Price calculation
        Map<Integer, Double> bookMasterDataMap = bookStoreService.getMasterBookPriceMapCopy();

        //process set with standard discount rate
        double calculatedTotalDiscount = bookDiscList.stream()
                .mapToDouble(bookSet -> {
                    double totalBookPrice = bookSet.stream()
                            .mapToDouble(bookId -> bookMasterDataMap.getOrDefault(bookId, 0.0))
                            .sum();

                    int eligibleDiscountRate = getDiscount(bookSet.size());
                    return (eligibleDiscountRate / TOTAL_PERCENT) * totalBookPrice;
                })
                .sum();

        return Math.max(calculatedTotalDiscount, maxDiscount);

    }

    private List<CartBook> consolidateCart(List<CartBook> cartBooks) {
        return cartBooks.stream()
                .collect(Collectors.toMap(
                        CartBook::bookId,
                        book -> book, // Keep the entire book object as the starting value
                        (existing, replacement) -> new CartBook(
                                existing.bookId(),
                                existing.title(),
                                existing.copies() + replacement.copies() // Sum the copies together
                        )
                ))
                .values().stream()
                .toList();
    }

}
