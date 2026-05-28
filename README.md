# Software Development Books Store

There is a series of books about software development that have been read by a lot of developers who want to improve their development skills. Let’s say an editor, in a gesture of immense generosity to mankind (and to increase sales as well), is willing to set up a pricing model where you can get discounts when you buy these books. The available books are :

Clean Code (Robert Martin, 2008)
The Clean Coder (Robert Martin, 2011)
Clean Architecture (Robert Martin, 2017)
Test Driven Development by Example (Kent Beck, 2003)
Working Effectively With Legacy Code (Michael C. Feathers, 2004)

# Purpose

The Software development books store api is developed using Test Driven Development approach in Java programming language that helps in calculating the total amount of the books selected to purchase after discount.

# Rules
```
The rules are described below :

One copy of the five books costs 50 EUR.

* If, however, you buy two different books from the series, you get a 5% discount on those two books.
* If you buy 3 different books, you get a 10% discount.
* With 4 different books, you get a 20% discount.
* If you go for the whole hog, and buy all 5, you get a huge 25% discount.
* Note that if you buy, say, 4 books, of which 3 are different titles, you get a 10% discount on the 3 that form part of a set, but the 4th book still costs 50 EUR.
```
# Functional Cases:
```
While calculating the discount it should calculate all possible combination to get highest discount amount that can be offered to the developer. 
```

# Prerequisites
To run this program below softwares needs to be installed
```
Java - Version 21 or above
JRE compliance - 21 or above
Maven - For Dependency management
JUnit - Version 6.0.3 (auto imported with Springboot 4.0.6 in pom.xml)
IntelliJ - Any IDE which supports Java
```

# Steps to run the test cases in IntelliJ IDE
```
1) Download project as zip file and unzip the same to a folder. 
2) In IntelliJ IDE,  Go to the below path
   File -> Import -> select Existing Maven Project option -> Next -> 
   Browse 	extracted folder and Finish
2.1) Or you can open IntelliJ IDE --> Menu New --> Project from Version Control
and enter https://github.com/vishalkatatest/ebookstore-kata in the Url paramter  
3) Then, Right click project in Project Explorer window and 
   select Run All Test
```
# How to access the code coverage report
```
Jacoco code coverage report can be accessed by the following steps.

Download project as zip file and unzip the same to a folder. 
Open a command prompt and goto the project(extracted) folder and execute below commands:
mvn clean
mvn install
After completion of above steps, the report will be available in below path
<PROJECT_ROOT_FOLDER>\target\site\jacoco\index.html
```
# Passing input values - Guidelines
```
1) Your inputs should be in the format of book object as below:
   {
      "bookId": 1001,
      "title": "Clean Code",
      "copies": 2
    }
2) Where book Id can range from 1001 to 1005
Book ID: 1001 Title: "Clean Code"
Book ID: 1002 Title: "The Clean Coder"
Book ID: 1003 Title: "Clean Architecture"
Book ID: 1004 Title: "Test Driven Development by Example"
Book ID: 1005 Title: "Working effectively with Legacy Code"

3) Book Price will remain 50 as given in problem statement    
```
# Steps to run the application in Postman
```

After successful maven build

In IntelliJ right click on EbookstoreApplication --> Run EbookstoreApplication.main()
It will start tomcat server on port 8080 by default

Now open Postman tool.
1) Select method type as Post
2) Enter Url as localhost:8080/cart/checkout
3) Go to Body tab and select raw in the dropdown that allows you to enter Json format data
4) Enter Json input data in below format
{
  "books": [
    {
      "bookId": 1001,
      "title": "Clean Code",
      "copies": 2
    },
    {
      "bookId": 1002,
      "title": "Clean Architecture",
      "copies": 1
    },
    {
      "bookId": 1003,
      "title": "The Clean Coder",
      "copies": 2
    }
  ]
}

It will return client with below response format:
{
    "cartAmount": 230.0,
    "discountAmount": 20.0
}

5) When you pass empty cart like below:
 {
  "books": []
}

It will return below response to client:
{
    "messageCode": "EMPTY_CART",
    "errorMessage": "No books are selected to process the cart"
}


6) When Invalid book is passed, it will return client with below format response:
{
    "messageCode": "INVALID_BOOK",
    "errorMessage": "Validation Failed: Book ID 1008 does not exist in the master catalog."
}