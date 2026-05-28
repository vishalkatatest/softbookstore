package com.softkata.softbookstore.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CartResponse {
    double cartAmount;
    double discountAmount;
    String errorMessage;
}
