package com.enotes.Enotes_INDUS.service.impl;

import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
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
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*20))
                .and()
                .signWith(getKey())

                .compact();
        return token;
    }
    private Key getKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    @Override
    public String extractUsername(String token) {
        Claims claims= extractAllClaims(token);
        return claims.getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username=extractUsername(token);
        Boolean isExpired=isTokenExpired(token);
        if(username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired){
            return true;
        }
        return false;
    }

    private Boolean isTokenExpired(String token) {
        Claims claims=extractAllClaims(token);
        Date expiredDate=claims.getExpiration();

        return expiredDate.before(new Date());
    }


}
