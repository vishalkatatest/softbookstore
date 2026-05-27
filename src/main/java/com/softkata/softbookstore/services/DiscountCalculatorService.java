package com.softkata.softbookstore.services;

import org.springframework.stereotype.Service;

@Service
public class DiscountCalculatorService {

    public int getDiscount(int totalBooks) {

        return  switch (totalBooks) {
            case 2 -> 5;
            case 3 -> 10;
            case 4 -> 20;
            case 5 -> 25;
            default -> 0;
        };

    }
}
