package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtServiceImpl implements JwtService {
    private String secretKey="";

    public JwtServiceImpl(){
        try {
            KeyGenerator keyGenerator=KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk=keyGenerator.generateKey();
            secretKey=  Base64.getEncoder().encodeToString(sk.getEncoded());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String generateToken(User user) {

        Map<String,Object> claims=new HashMap<>();
        claims.put("id",user.getId());
        claims.put("role",user.getRole());
        claims.put("status",user.getAccountStatus().getIsActive());

        String token= Jwts.builder()
                .claims().add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()*60*60*20))
                .and()
                .signWith(getKey())

                .compact();
        return token;
    }

    private Key getKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
