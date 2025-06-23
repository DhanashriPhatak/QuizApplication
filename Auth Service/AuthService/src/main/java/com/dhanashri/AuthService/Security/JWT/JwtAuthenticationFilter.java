package com.dhanashri.AuthService.Security.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter()
    {
        System.out.println("Jwt auhhentication filter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        System.out.println("Reveal path:-"+path);
        if (path.equals("/auth/login") || path.equals("/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        System.out.println("authorization header:-"+authHeader);
        //check if header contains the valid bearer token
        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            System.out.println("❌ Missing or invalid Bearer token");
            filterChain.doFilter(request,response);
            return;
        }

        jwt = authHeader.substring(7);//remove "Bearer "
        userEmail = jwtUtils.getEmailFromToken(jwt);
        System.out.println("📧 Extracted email from token: " + userEmail);
        //if user is not yet authenticated
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if(jwtUtils.isTokenValid(jwt,userDetails))
            {
                System.out.println("✅ Token is valid. Setting authentication context for user: " + userEmail);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else {
                System.out.println("⚠️ Invalid token for user: " + userEmail);
            }
        }
        System.out.println("✅ Proceeding with filter chain");
        filterChain.doFilter(request,response);
    }

}
