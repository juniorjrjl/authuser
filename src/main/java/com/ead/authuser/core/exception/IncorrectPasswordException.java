package com.ead.authuser.core.exception;

public class IncorrectPasswordException extends AuthUserException{

    public IncorrectPasswordException(final String message) {
        super(message);
    }
}
