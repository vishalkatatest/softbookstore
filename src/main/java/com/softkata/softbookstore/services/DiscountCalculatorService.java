package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.DiscountProperties;
import org.springframework.stereotype.Service;

@Service
public class DiscountCalculatorService {

    DiscountProperties discountProperties;

    public DiscountCalculatorService(DiscountProperties discountProperties) {
        this.discountProperties = discountProperties;
    }

    public int getDiscount(int totalBooks) {

        return  switch (totalBooks) {
            case 2 -> discountProperties.getDiscountFor2Books();
            case 3 -> discountProperties.getDiscountFor3Books();
            case 4 -> discountProperties.getDiscountFor4Books();
            case 5 -> discountProperties.getDiscountFor5Books();
            default -> 0;
        };

    }
}
