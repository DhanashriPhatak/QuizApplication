//package com.dhanashri.API_gateway.Security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.JwtException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//@Order(-100)
//public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
//
//    @Value("${app.jwt.secret}")
//    private String jwtSecret;
//
//    private boolean isAuthWhitelisted(String path) {
//        return path.startsWith("/authservice/auth/");
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getURI().getPath();
//        System.out.println("🛡️ [JWT Filter] Incoming path: " + path);
//
//        if (isAuthWhitelisted(path)) {
//            return chain.filter(exchange); // Skip auth service
//        }
//
//        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        String token = authHeader.substring(7);
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(jwtSecret)
//                    .parseClaimsJws(token)
//                    .getBody();
//            System.out.println("✅ JWT Token is valid. User: " + claims.getSubject());
//        } catch (JwtException e) {
//            System.err.println("❌ Invalid JWT Token: " + e.getMessage());
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return -1; // Highest precedence
//    }
//}
//
//
//
////@Component
////public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
////
////    @Autowired
////    private JwtUtils jwtUtils;
////
////
////
////    @Override
////    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
////        System.out.println("✅ JwtAuthenticationFilter invoked");
////        return chain.filter(exchange);
//////        String path = exchange.getRequest().getURI().getPath();
//////        HttpMethod method = exchange.getRequest().getMethod();
//////
//////        System.out.println("Method: " + method + ", Path: " + path);
//////
//////        // 🔁 1. Bypass preflight OPTIONS requests
//////        if (method == HttpMethod.OPTIONS) {
//////            System.out.println("🟡 Skipping JWT check for OPTIONS request");
//////            return chain.filter(exchange);
//////        }
//////
//////        // 🔁 2. Bypass login/register endpoints
//////        if (path.startsWith("/authservice/auth")) {
//////            System.out.println("🟡 Skipping JWT check for authservice path");
//////            return chain.filter(exchange);
//////        }
//////
//////        // 🔐 3. Check JWT Authorization header
//////        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//////        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//////            System.out.println("❌ Missing or invalid Authorization header");
//////            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//////            return exchange.getResponse().setComplete();
//////        }
//////
//////        String token = authHeader.substring(7);
//////        try {
//////            jwtUtils.validateToken(token); // assuming you use jwtUtils here
//////            System.out.println("✅ Token is valid");
//////            return chain.filter(exchange);
//////        } catch (Exception ex) {
//////            System.out.println("❌ Invalid JWT: " + ex.getMessage());
//////            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//////            return exchange.getResponse().setComplete();
//////        }
////    }
////
////
////    @Override
////    public int getOrder() {
////        return -1; // Ensure this runs early
////    }
////}
