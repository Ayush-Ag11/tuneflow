package com.tuneflow.auth_service.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String userName) {
        super("Username already taken: " + userName);
    }
}
