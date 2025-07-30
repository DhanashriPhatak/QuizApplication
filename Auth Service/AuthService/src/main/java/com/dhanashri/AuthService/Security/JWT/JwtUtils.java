package com.dhanashri.AuthService.Security.JWT;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@RefreshScope
@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey()
    {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    //Generate JWT Token
    public String generateToken(String email,String role)
    {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Extract Email
    public String getEmailFromToken(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //Validate JWT token
    public boolean validateToken(String token)
    {
        try{
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        }
        catch(ExpiredJwtException ex)
        {
            System.err.println("JWT expired: " + ex.getMessage());
        }catch (UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.err.println("Malformed JWT: " + ex.getMessage());
        } catch (SignatureException ex) {
            System.err.println("Invalid signature: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("Illegal arg in JWT: " + ex.getMessage());
        }
        return false;
    }

    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String userName = getEmailFromToken(token);
        return userName.equals(userDetails.getUsername()) && validateToken(token);
    }
}
