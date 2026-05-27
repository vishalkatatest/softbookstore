package com.softkata.softbookstore;

import com.softkata.softbookstore.domain.CartBook;

public class BookTestDataProvider {

    private static final int BOOK1_ID = 1001;
    private static final String BOOK1_TITLE = "Clean Code";

    private static final int BOOK2_ID = 1002;
    private static final String BOOK2_TITLE = "The Clean Coder";

    private static final int BOOK3_ID = 1003;
    private static final String BOOK3_TITLE = "Clean Architecture";

    private static final int BOOK4_ID = 1004;
    private static final String BOOK4_TITLE = "Test Driven Development by Example";

    private static final int BOOK5_ID = 1005;
    private static final String BOOK5_TITLE = "Working effectively with Legacy Code";

    public CartBook addDummyCodeBook(int numOfCopies) {
        return new CartBook(BOOK1_ID, BOOK1_TITLE, numOfCopies);
    }

    public CartBook addDummyCoderBook(int numOfCopies) {
        return new CartBook(BOOK2_ID, BOOK2_TITLE, numOfCopies);
    }

    public CartBook addDummyCleanArchitectureBook(int numOfCopies) {
        return new CartBook(BOOK3_ID, BOOK3_TITLE, numOfCopies);
    }

    public CartBook addDummyTDDByExampleBook(int numOfCopies) {
        return new CartBook(BOOK4_ID, BOOK4_TITLE, numOfCopies);
    }

    public CartBook addDummyWorkingWithLegacyBook(int numOfCopies) {
        return new CartBook(BOOK5_ID, BOOK5_TITLE, numOfCopies);
    }
}
