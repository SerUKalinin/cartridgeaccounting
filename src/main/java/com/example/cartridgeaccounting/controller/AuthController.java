package com.example.cartridgeaccounting.controller;

import com.example.cartridgeaccounting.util.JwtUtil;
import com.example.cartridgeaccounting.dto.AuthRequest;
import com.example.cartridgeaccounting.dto.AuthResponse;
import com.example.cartridgeaccounting.dto.CreateUserRequest;
import com.example.cartridgeaccounting.service.CustomUserDetailsService;
import com.example.cartridgeaccounting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            return ResponseEntity.ok(new AuthResponse(token, role));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Неверный логин или пароль");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        try {
            userService.createUser(request);
            return ResponseEntity.ok("Пользователь успешно зарегистрирован");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка регистрации: " + e.getMessage());
        }
    }
} 