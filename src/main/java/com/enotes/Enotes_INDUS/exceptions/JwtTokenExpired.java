package com.enotes.Enotes_INDUS.exceptions;

public class JwtTokenExpired extends RuntimeException {
    public JwtTokenExpired(String message) {
        super(message);
    }
}
