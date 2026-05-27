package com.softkata.softbookstore.services;

import com.softkata.softbookstore.domain.DiscountData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DiscountRulesServiceTests {

    @Autowired
    DiscountRulesService discountRulesService;

    @Test
    public void getAllDiscountRulesMasterData() {
        List<DiscountData> discountRules = this.discountRulesService.getDiscountRules();
        assertNotNull(discountRules);
    }
}
