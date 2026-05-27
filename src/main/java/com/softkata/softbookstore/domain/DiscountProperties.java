package com.softkata.softbookstore.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "book-discount")
@Data
public class DiscountProperties {
    private int discountFor2Books;
    private int discountFor3Books;
    private int discountFor4Books;
    private int discountFor5Books;
}
