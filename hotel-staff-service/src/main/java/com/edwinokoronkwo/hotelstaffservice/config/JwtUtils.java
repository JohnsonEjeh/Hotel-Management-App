//package com.edwinokoronkwo.hotelstaffservice.config;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtUtils {
//
//    @Value("${jwt.secret}") // Load from application.properties or application.yml
//    private String jwtSecret;
//
//    @Value("${jwt.expirationMs}") // Load token expiration time
//    private long jwtExpirationMs;
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
//    }
//
//    // Generate JWT token
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // Extract username from JWT token
//    public String extractUsername(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    // Validate JWT token
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (ExpiredJwtException e) {
//            System.out.println("JWT token is expired: " + e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            System.out.println("JWT token is unsupported: " + e.getMessage());
//        } catch (MalformedJwtException e) {
//            System.out.println("JWT token is malformed: " + e.getMessage());
//        } catch (SignatureException e) {
//            System.out.println("JWT signature validation failed: " + e.getMessage());
//        } catch (IllegalArgumentException e) {
//            System.out.println("JWT claims string is empty: " + e.getMessage());
//        }
//        return false;
//    }
//}
