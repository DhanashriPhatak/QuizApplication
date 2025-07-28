//package com.dhanashri.API_gateway.Security;
//
//import com.netflix.discovery.converters.Auto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http)
//    {
//        System.out.println("Inside security config");
//        return http
//                .csrf(csrf->csrf.disable())
//                .authorizeExchange(exchange->exchange
//                        .pathMatchers("/authservice/auth/**").permitAll()
//                        .anyExchange().authenticated()
//                )
////                .addFilterAt(jwtAuthenticationFilter,SecurityWebFiltersOrder.AUTHENTICATION)
//                .build();
//    }
//}
