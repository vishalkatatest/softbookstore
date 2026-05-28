package com.softkata.softbookstore.utils;

import com.softkata.softbookstore.domain.CartBook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CartUtils {


    public static Map<Integer, Integer> buildCartBookCopyMap(List<CartBook> cartBooks) {
        return cartBooks.stream()
                .collect(Collectors.toMap(
                        CartBook::bookId,
                        CartBook::copies
                ));
    }

    public static List<CartBook> consolidateCart(List<CartBook> cartBooks) {
        return cartBooks.stream()
                .collect(Collectors.toMap(
                        CartBook::bookId,
                        book -> book,
                        (existing, replacement) -> new CartBook(
                                existing.bookId(),
                                existing.title(),
                                existing.copies() + replacement.copies()
                        )
                ))
                .values().stream()
                .toList();
    }


}
