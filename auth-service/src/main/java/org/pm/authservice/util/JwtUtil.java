package org.pm.authservice.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String base64Key) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + (1000 * 60 * 60 * 10); // 10 hours

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date(nowMillis))
                .expiration(new Date(expMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256) // ✅ modern usage
                .compact();
    }

    public void validateToken(String token) {
        try{
            Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token);

        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token");
        }
    }
}
