package com.airlineapi.controller;

import com.airlineapi.service.AuthService;
import com.airlineapi.service.UserDetailsImpl;
import com.airlineapi.service.CheckRequest;
import com.airlineapi.service.RegisterRequest;
import com.airlineapi.service.MessageResponse;
import com.airlineapi.service.UserResponce;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Kullanıcı oturum açma işlemi
     */
    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody CheckRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    /**
     * Admin oturum açma işlemi
     */
    @PostMapping("/admin/login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody CheckRequest loginRequest) {
        return authService.authenticateAdmin(loginRequest);
    }


    /**
     * Kullanıcı kayıt işlemi
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

    /**
     * Kullanıcı oturum kapatma işlemi
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout() {
        return authService.signoutUser();
    }

    /**
     * Kullanıcı kimlik doğrulama durumu kontrolü
     */
    @GetMapping("/isAuthenticated")
    public boolean isAuthenticated(HttpServletRequest request) {
        return authService.isAuthenticated(request);
    }

    /**
     * Kullanıcı detaylarını al
     */
    @GetMapping("/userDetails")
    public ResponseEntity<Object> getUserDetails(Authentication authentication) {
        return authService.getUserDetails(authentication);
    }
}
