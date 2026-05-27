package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookStoreServiceTests {

    @Autowired
    BookStoreService bookStoreService;

    @Test
    public void getAllBooksMasterdata() {
        List<Book> books = this.bookStoreService.getBookMasterData();
        assertNotNull(books);
    }
}
