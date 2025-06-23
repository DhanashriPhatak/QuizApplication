package com.dhanashri.AuthService.Service;

import com.dhanashri.AuthService.DTO.Request.LoginRequest;
import com.dhanashri.AuthService.DTO.Request.RegisterRequest;
import com.dhanashri.AuthService.DTO.Response.AuthResponse;
import com.dhanashri.AuthService.Module.Role;
import com.dhanashri.AuthService.Module.User;
import com.dhanashri.AuthService.Repository.RoleRepository;
import com.dhanashri.AuthService.Repository.UserRepository;
import com.dhanashri.AuthService.Security.JWT.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public String authenticate(String email,String password)
    {
        //perform authentication (will throw error if invalid)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));

        //load user and generate token
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        return jwtUtils.generateToken(user.getEmail());
    }

    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
        try{
            //check if user exists
            if(userRepository.findByEmail(registerRequest.getEmail()).isPresent())
            {
                throw new RuntimeException("Email already Registered");
            }

            //fetch default role
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(()->new RuntimeException("Default role not found"));

            //create user
            User user = User.builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .roles(Set.of(defaultRole))
                    .enabled(true)
                    .build();
            userRepository.save(user);

            //generate token
            String token = jwtUtils.generateToken(user.getEmail());
            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Register failed",e);
        }
    }

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        try{
            System.out.println("📦 Inside AuthService login for: " + loginRequest.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),loginRequest.getPassword()
                    )
            );

            //if successful
            String token = jwtUtils.generateToken(loginRequest.getEmail());
            User user = userRepository.findByEmail(loginRequest.getEmail()).get();
            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Login failed", e);
        }
    }
}
