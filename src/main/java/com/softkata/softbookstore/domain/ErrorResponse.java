package com.softkata.softbookstore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    String messageCode;
    String errorMessage;
}
