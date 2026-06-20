package com.tuneflow.auth_service.exception;

public class AccountAlreadyVerifiedException extends RuntimeException {
    public AccountAlreadyVerifiedException() {
        super("This account is already verified. Please log in.");
    }
}
