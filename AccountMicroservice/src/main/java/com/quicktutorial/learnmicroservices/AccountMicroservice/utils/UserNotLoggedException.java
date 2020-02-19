package com.quicktutorial.learnmicroservices.AccountMicroservice.utils;

public class UserNotLoggedException extends Exception {

    public UserNotLoggedException(String errorMessage) {
        super(errorMessage);
    }
}
