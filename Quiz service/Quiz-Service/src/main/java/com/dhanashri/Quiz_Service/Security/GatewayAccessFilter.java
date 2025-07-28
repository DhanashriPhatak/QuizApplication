//package com.dhanashri.Quiz_Service.Security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class GatewayAccessFilter extends OncePerRequestFilter {
//
//    @Value("${gateway.secret}")
//    private String SECRET;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String gatewayHeader = request.getHeader("X-Gateway-Secret");
//
//        if(!SECRET.equals(gatewayHeader))
//        {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.getWriter().write("Access Denied: Request not from API Gateway");
//            return;
//        }
//
//        filterChain.doFilter(request,response);
//    }
//}
