package com.dhanashri.AuthService.Controller;

import com.dhanashri.AuthService.DTO.Request.LoginRequest;
import com.dhanashri.AuthService.DTO.Request.RegisterRequest;
import com.dhanashri.AuthService.DTO.Response.AuthResponse;
import com.dhanashri.AuthService.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest)
    {
        return authService.register(registerRequest);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest)
    {
        System.out.println("🔐 Inside login controller");
        return authService.login(loginRequest);
    }
}
