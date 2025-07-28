//package com.dhanashri.API_gateway.Security;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@Component
//public class LoggingGlobalFilter implements GlobalFilter, Ordered {
//
//    @Autowired
//    private List<GlobalFilter> filters;
//
//    @PostConstruct
//    public void printFilters() {
//        System.out.println("🧪 Registered Global Filters:");
//        filters.forEach(f -> System.out.println("➡️ " + f.getClass().getName()));
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        System.out.println("🧪 LoggingGlobalFilter triggered for: " + exchange.getRequest().getURI().getPath());
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return -200;
//    }
//}
//
