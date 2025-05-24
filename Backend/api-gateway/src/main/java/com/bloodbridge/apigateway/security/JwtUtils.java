package com.bloodbridge.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object role = claims.get("role");
        if (role == null) return "USER";
        return role.toString().toUpperCase();
    }
    
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception e1) {
            // Try with raw secret as fallback
            try {
                Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())
                    .build()
                    .parseClaimsJws(authToken);
                return true;
            } catch (Exception e2) {
                // Try with base64 encoded secret
                try {
                    Jwts.parserBuilder()
                        .setSigningKey(java.util.Base64.getEncoder().encode(jwtSecret.getBytes()))
                        .build()
                        .parseClaimsJws(authToken);
                    return true;
                } catch (Exception e3) {
                    // Try with base64 decoded secret
                    try {
                        byte[] decodedKey = java.util.Base64.getDecoder().decode(jwtSecret);
                        Jwts.parserBuilder()
                            .setSigningKey(decodedKey)
                            .build()
                            .parseClaimsJws(authToken);
                        return true;
                    } catch (Exception e4) {
                        return false;
                    }
                }
            }
        }
    }
}
