package com.softkata.softbookstore.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.book-store")
public record BookStoreProperties(List<Book> books) {}
