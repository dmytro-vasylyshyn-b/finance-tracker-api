package com.vasylyshyn.finanseTracker.Services;

import com.vasylyshyn.finanseTracker.Entitys.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Users user) {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return token.replace("+", "-").replace("/", "_").replace("=", "");
    }

    public String extractUsername(String token) {
        token = token.replace("-", "+").replace("_", "/");
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token, Optional<Users> userDetails) {
        return extractUsername(token).equals(userDetails.get().getEmail());
    }
}


