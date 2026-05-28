package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.DiscountData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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

        //Get total number of books in the cart
        int totalBooksInCart = cartBooks.stream().mapToInt(CartBook::copies).sum();

        //This map is created to have copy of book list and used to distribute each book copy under different sets
        Map<Integer, Integer> initialCopiesMap = getCartBookCopyMap(cartBooks);


        //This will look for various possibilities to get best discount amount
        //If only one distinct book present in the list then it will straight go to return statement with 0 discount
        double bestDiscount = IntStream
                .rangeClosed(2, distinctBookCnt)
                .mapToDouble(groupSize ->
                        calculateDiscountForGroupSize(
                                groupSize,
                                totalBooksInCart,
                                initialCopiesMap,
                                cartBooks
                        ))
                .max()
                .orElse(0);

        return BigDecimal.valueOf(bestDiscount)
                .setScale(DECIMAL_ROUND_OFF, RoundingMode.HALF_UP)
                .doubleValue();

    }


    private double calculateDiscountForGroupSize(
            int groupSize,
            int totalBooks,
            Map<Integer, Integer> initialCopiesMap,
            List<CartBook> cartBooks) {

        Map<Integer, Integer> currentBookCopiesMap = new HashMap<>(initialCopiesMap);

        List<Set<Integer>> groupedSets =
                buildBookSets(groupSize, totalBooks, currentBookCopiesMap, cartBooks);

        return processMaxDiscount(groupedSets);
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

        //Reduce copies count by 1 after set is prepared
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


    private double processMaxDiscount(List<Set<Integer>> bookDiscList) {

        //get Book Master data map for Price calculation
        Map<Integer, Double> bookMasterDataMap = bookStoreService.getMasterBookPriceMapCopy();

        //process set with standard discount rate
        return bookDiscList.stream()
                .mapToDouble(bookSet -> {
                    double totalBookPrice = bookSet.stream()
                            .mapToDouble(bookId -> bookMasterDataMap.getOrDefault(bookId, 0.0))
                            .sum();

                    int eligibleDiscountRate = getDiscount(bookSet.size());
                    return (eligibleDiscountRate / TOTAL_PERCENT) * totalBookPrice;
                })
                .sum();

        //return Math.max(calculatedTotalDiscount, maxDiscount);

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
