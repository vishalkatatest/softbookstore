package com.softkata.softbookstore.controller;

import com.softkata.softbookstore.domain.Book;
import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartResponse;
import com.softkata.softbookstore.services.BookCartService;
import com.softkata.softbookstore.services.BookStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class BookCartController {

    BookCartService bookCartService;
    BookStoreService bookStoreService;

    public BookCartController(BookCartService bookCartService, BookStoreService bookStoreService) {

        this.bookCartService = bookCartService;
        this.bookStoreService = bookStoreService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CartResponse> processCart(@RequestBody Cart bookCart) {
            return ResponseEntity.ok(this.bookCartService.processCartAmount(bookCart));
    }

    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getBookMasterData() {
        return ResponseEntity.ok(this.bookStoreService.getBookMasterData());
    }

}
