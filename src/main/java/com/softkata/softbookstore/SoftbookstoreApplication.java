package com.softkata.softbookstore;

import com.softkata.softbookstore.domain.BookStoreProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(BookStoreProperties.class)
public class SoftbookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftbookstoreApplication.class, args);
    }

}
