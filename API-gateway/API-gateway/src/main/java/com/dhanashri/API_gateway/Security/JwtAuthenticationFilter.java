package com.dhanashri.API_gateway.Security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private boolean isValidToken(String token)
    {
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }
        catch(JwtException e)
        {
            return false;
        }
    }



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("Jwt Filter invoked"); // Debug

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/authservice/auth/")) {
            return chain.filter(exchange); // Allow auth service paths
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(builder -> builder.header("X-Gateway-Secret","gTw!3s7x8@APIOnly"))
                        .build();
                return chain.filter(mutatedExchange); // Valid token
            } catch (JwtException e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        System.out.println("After if");
        // No token or invalid format
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Ensure it's one of the first filters to run
    }
}

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//
//        if(authHeader != null && authHeader.startsWith("Bearer ")){
//            String jwt = authHeader.substring(7);
//            if(!isValidToken(jwt))
//            {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid Jwt token");
//                return;
//            }
//        }
//        else {
//            String path = request.getRequestURI();
//            if(!path.contains("/auth/login") && !path.contains("/auth/register"))
//            {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Missing Authorization header");
//                return;
//            }
//        }
//
//        filterChain.doFilter(request,response);
//    }