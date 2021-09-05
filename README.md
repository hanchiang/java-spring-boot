
## Introduction
This project is a simple Java REST API service built with Spring Boot, Spring Security, MySQL, H2, JUnit, Rest Assured 

## Set up
* Install [Java](https://www.oracle.com/sg/java/technologies/javase/javase-jdk8-downloads.html)
* Install [MySQL](https://www.mysql.com/), configure the relevant properties in `resources/application.properties`
* Install [H2]https://www.h2database.com/html/main.html

## Running tests

### Unit tests
Files that have "Test"(e.g. UserControllerTest.java) at the end are unit tests.

### Integration tests
Files that have "IT"(e.g. UserControllerIT.java) at the end are integration tests, which use h2.

**Configuration for h2**
* spring.h2.console.enabled=true
* spring.jpa.hibernate.ddl-auto=update

