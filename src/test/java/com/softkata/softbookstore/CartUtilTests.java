package com.softkata.softbookstore;

import com.softkata.softbookstore.domain.CartBook;
import com.softkata.softbookstore.utils.CartUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CartUtilTests {

    BookTestDataProvider bookTestDataProvider;

    @BeforeEach
    public void setupData() {
        bookTestDataProvider = new BookTestDataProvider();
    }

    @Test
    public void checkIfCartIsConsolidatedForRepeatedBooksInCart() {
        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(1),
                bookTestDataProvider.addDummyCoderBook(1),
                bookTestDataProvider.addDummyCodeBook(2)
        );

        List<CartBook> result =
                CartUtils.consolidateCart(books);

        assertEquals(2, result.size());
    }

    @Test
    void shouldCreateBookCopiesMap() {

        List<CartBook> books = List.of(
                bookTestDataProvider.addDummyCodeBook(3),
                bookTestDataProvider.addDummyCoderBook(2)
        );

        Map<Integer, Integer> result =
                CartUtils.buildCartBookCopyMap(books);

        assertEquals(3, result.get(1001));
        assertEquals(2, result.get(1002));
    }
}
