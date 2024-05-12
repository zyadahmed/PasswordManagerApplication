package com.example.passwordmanager.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    SecretKey key;

    @PostConstruct
    private Key generateKey(){

         key = Jwts.SIG.HS256.key().build();
        return key;
    }

    public String generateToken(UserDetails user){
        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .claim("Role",user.getAuthorities())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(key).compact();
        return token;
    }

    public boolean isTokenValid(String token){
       Claims claims = (Claims) Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return claims.getExpiration().after(new Date());

    }
    public Claims getClaims(String token){
        Claims claims = (Claims) Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return claims;

    }
    public <T> T extractClaim(String token, Function<Claims,T> cliamResolver){
        final Claims claims = getClaims(token);
        return cliamResolver.apply(claims);
    }

    private Date extractExpration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
    public String extractUserEmail(String token) {
        return  extractClaim(token,Claims::getSubject);
    }
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractUserEmail(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        return extractExpration(token).before(new Date());
    }











}
