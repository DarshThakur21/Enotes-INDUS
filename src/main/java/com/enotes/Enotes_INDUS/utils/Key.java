package com.enotes.Enotes_INDUS.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Key {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk=keyGenerator.generateKey();
       String secretKey=  Base64.getEncoder().encodeToString(sk.getEncoded());
        System.out.println(secretKey);
    }
}
