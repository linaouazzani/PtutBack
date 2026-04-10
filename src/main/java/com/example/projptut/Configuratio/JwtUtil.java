package com.example.projptut.Configuratio;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private final long EXPIRATION_MS;

    // Constructeur pour injecter la clé et l'expiration depuis application.properties
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expirationMs}") long expirationMs) {
        // Décoder la clé Base64 et vérifier sa longueur
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        if (keyBytes.length < 32) { // 32 octets = 256 bits minimum
            throw new IllegalArgumentException("La clé JWT doit faire au moins 256 bits (32 octets)");
        }
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
        this.EXPIRATION_MS = expirationMs;
    }

    // Générer un token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraire l'email depuis le token
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Valider le token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}