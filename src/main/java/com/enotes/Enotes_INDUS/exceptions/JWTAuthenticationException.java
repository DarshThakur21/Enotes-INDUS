package com.enotes.Enotes_INDUS.exceptions;

public class JWTAuthenticationException extends RuntimeException{

    public JWTAuthenticationException(String message) {
        super(message);
    }
}
