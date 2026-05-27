package com.softkata.softbookstore.controller;

import com.softkata.softbookstore.domain.Cart;
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
    public ResponseEntity<String> processCart(@RequestBody Cart bookCart) {
        try {
            double processedCartAmount = this.bookCartService.processCartAmount(bookCart);
            return ResponseEntity.ok(String.valueOf(processedCartAmount));
        } catch(IllegalArgumentException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
