package com.cards.cards.utils;

import com.cards.cards.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    private byte[] getSigningKeyBytes() {
        return Base64.getEncoder().encode(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Configure the ObjectMapper if needed
        return objectMapper;
    }

    private Serializer<Map<String, ?>> createObjectMapperSerializer(ObjectMapper objectMapper) {
        return new Serializer<>() {
            @Override
            public byte[] serialize(Map<String, ?> claims) throws JwtException {
                try {
                    return objectMapper.writeValueAsBytes(claims);
                } catch (JsonProcessingException e) {
                    throw new JwtException("Error serializing claims", e);
                }
            }
        };
    }



    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000);

        ObjectMapper objectMapper = createObjectMapper();
        Serializer<Map<String, ?>> serializer = createObjectMapperSerializer(objectMapper);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, getSigningKeyBytes())
                .serializeToJsonWith(serializer) // Use the custom serializer
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSigningKeyBytes()).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKeyBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String extractEmail(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKeyBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
