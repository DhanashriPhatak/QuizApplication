//package com.dhanashri.API_gateway.Security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtUtils {
//    @Value("${app.jwt.secret}")
//    private String jwtSecret;
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            System.out.println("🔍 Validating token: " + token);
//            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            System.err.println("❌ Invalid JWT: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public String getEmailFromToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//
//    public Claims getClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
