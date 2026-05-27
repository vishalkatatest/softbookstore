package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Book;
import com.softkata.softbookstore.domain.BookStoreProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookStoreService {
    private final BookStoreProperties bookStoreProperties;

    public BookStoreService(BookStoreProperties bookStoreProperties) {
        this.bookStoreProperties = bookStoreProperties;
    }

    public List<Book> getBookMasterData() {
        return bookStoreProperties.books();
    }

    public Map<Integer, Double> getMasterBookPriceMapCopy() {
        return bookStoreProperties.books().stream()
                .collect(Collectors.toMap(
                        Book::bookId,
                        Book::price
                ));
    }
}
