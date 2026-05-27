package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.DiscountData;
import com.softkata.softbookstore.domain.DiscountProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountRulesService {

    DiscountProperties discountProperties;

    public DiscountRulesService(DiscountProperties discountProperties) {
        this.discountProperties = discountProperties;
    }

    public List<DiscountData> getDiscountRules() {
        return this.discountProperties.discountData();
    }

}
