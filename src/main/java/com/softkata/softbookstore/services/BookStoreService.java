package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Book;
import com.softkata.softbookstore.domain.BookStoreProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookStoreService {
    private final BookStoreProperties bookStoreProperties;

    public BookStoreService(BookStoreProperties bookStoreProperties) {
        this.bookStoreProperties = bookStoreProperties;
    }

    public List<Book> getBookMasterData() {
        return bookStoreProperties.books();
    }
}
