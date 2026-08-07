package com.example.authserver.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.authserver.security.CustomUserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails userDetails) {
    	  Map<String, Object> claims = new HashMap<>();

          if (userDetails instanceof CustomUserDetails customUser) {
              claims.put("role", customUser.getUser().getRole().name());
          }

          return Jwts.builder()
                  .claims(claims)
                  .subject(userDetails.getUsername())
                  .issuedAt(new Date())
                  .expiration(new Date(System.currentTimeMillis() + expiration))
                  .signWith(getKey())
                  .compact();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}