package com.softkata.softbookstore.controller;

import com.softkata.softbookstore.domain.Cart;
import com.softkata.softbookstore.domain.CartResponse;
import com.softkata.softbookstore.services.BookCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class BookCartController {

    BookCartService bookCartService;

    public BookCartController(BookCartService bookCartService) {

        this.bookCartService = bookCartService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CartResponse> processCart(@RequestBody Cart bookCart) {
            return ResponseEntity.ok(this.bookCartService.processCartAmount(bookCart));
    }

}
