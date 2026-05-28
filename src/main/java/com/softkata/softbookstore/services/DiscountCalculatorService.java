package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.domain.DiscountData;
import com.softkata.softbookstore.utils.CartUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.softkata.softbookstore.constants.Constant.*;


@Service
@RequiredArgsConstructor
public class DiscountCalculatorService {

    private final DiscountRulesService discountRulesService;
    private final BookStoreService bookStoreService;

    public int getDiscount(int totalBooks) {

        List<DiscountData> discountRules = discountRulesService.getDiscountRules();

        if (discountRules == null || discountRules.isEmpty() || totalBooks <= ZERO_INT) {
            return ZERO_INT;
        }

        return discountRules.stream()
                .filter(rule -> totalBooks == rule.noOfBooks())
                .mapToInt(DiscountData::discPercent)
                .findFirst()
                .orElse(ZERO_INT);

    }

    public double processDiscount(List<CartBook> cartBookData) {

        List<CartBook> cartBooks = CartUtils.consolidateCart(cartBookData);
        int distinctBookCnt = cartBooks.size();

        int totalBooksInCart = cartBooks.stream().mapToInt(CartBook::copies).sum();

        Map<Integer, Integer> initialCopiesMap = CartUtils.buildCartBookCopyMap(cartBooks);

        double bestDiscount = IntStream
                .rangeClosed(MIN_BOOKS_FOR_DISCOUNT, distinctBookCnt)
                .mapToDouble(groupSize ->
                        calculateDiscountForGroupSize(
                                groupSize,
                                totalBooksInCart,
                                initialCopiesMap,
                                cartBooks
                        ))
                .max()
                .orElse(ZERO_INT);

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

        if (remainingBooks <= ZERO_INT) {
            return Collections.emptyList();
        }

        Set<Integer> currentBookSet = createCurrentBookSet(groupSize, copiesMap, cartBooks);

        reduceBookCopies(currentBookSet, copiesMap);

        int consumed = currentBookSet.size();

        List<Set<Integer>> remainingBookSets =
                buildBookSets(
                        groupSize,
                        remainingBooks - consumed,
                        copiesMap,
                        cartBooks
                );

        return combineBookSets(currentBookSet, remainingBookSets);
    }

    private void reduceBookCopies(
            Set<Integer> currentBookSet,
            Map<Integer, Integer> copiesMap) {

        currentBookSet.forEach(bookId ->
                copiesMap.computeIfPresent(
                        bookId,
                        (k, v) -> v - DECREMENT_BY_ONE
                ));
    }

    private List<Set<Integer>> combineBookSets(
            Set<Integer> currentBookSet,
            List<Set<Integer>> remainingBookSets) {

        List<Set<Integer>> result = new ArrayList<>();

        result.add(currentBookSet);
        result.addAll(remainingBookSets);

        return result;
    }

    private Set<Integer> createCurrentBookSet(
            int groupSize,
            Map<Integer, Integer> copiesMap,
            List<CartBook> cartBooks) {

        return cartBooks.stream()
                .filter(book ->
                        copiesMap.getOrDefault(book.bookId(), ZERO_INT) > ZERO_INT)
                .limit(groupSize)
                .map(CartBook::bookId)
                .collect(Collectors.toSet());
    }

    private double processMaxDiscount(List<Set<Integer>> bookDiscList) {

        Map<Integer, Double> bookMasterDataMap = bookStoreService.getMasterBookPriceMapCopy();

        return bookDiscList.stream()
                .mapToDouble(bookSet -> {
                    double totalBookPrice = bookSet.stream()
                            .mapToDouble(bookId -> bookMasterDataMap.getOrDefault(bookId, ZERO_DOUBLE))
                            .sum();

                    int eligibleDiscountRate = getDiscount(bookSet.size());
                    return (eligibleDiscountRate / TOTAL_PERCENT) * totalBookPrice;
                })
                .sum();

    }



}
