package com.softkata.softbookstore.domain;

import java.util.List;

public record Cart(
        List<CartBook> books
) {
}
