package com.softkata.softbookstore.domain;

import lombok.Data;

@Data
public class CartResponse {
    double cartAmount;
    double discountAmount;
    String errorMessage;
}
