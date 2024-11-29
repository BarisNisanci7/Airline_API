package com.airlineapi.service;

import com.airlineapi.model.AppRole;
import com.airlineapi.model.Role;
import com.airlineapi.model.User;
import com.airlineapi.repository.RolesRepository;
import com.airlineapi.repository.UserRepository;
import com.airlineapi.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RolesRepository roleRepository;
    private final PasswordEncoder encoder;

    public ResponseEntity<Map<String, Object>> authenticateUser(CheckRequest loginRequest) {
        Authentication authentication = performAuthentication(loginRequest);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = extractRoles(userDetails);

        UserResponce response = new UserResponce(
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                jwtCookie.toString()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(Map.of("userInfo", response));
    }



    public ResponseEntity<Map<String, Object>> authenticateAdmin(CheckRequest loginRequest) {
        Authentication authentication = performAuthentication(loginRequest);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = extractRoles(userDetails);
        log.info(String.valueOf(roles));
        if (!roles.contains("ROLE_ADMIN")) {


            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Unauthorized", "status", false));
        }

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        UserResponce response = new UserResponce(
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                jwtCookie.toString()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(Map.of("userInfo", response));
    }

    public ResponseEntity<MessageResponse> registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail().toLowerCase())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Yeni kullanıcı oluştur
        User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());

        // Role atama işlemi
        String role = signUpRequest.getRole() == null || signUpRequest.getRole().isBlank()
                ? "USER" : signUpRequest.getRole();

        Set<Role> roles = new HashSet<>();
        roles.add(getOrCreateRole(AppRole.valueOf("ROLE_" + role.toUpperCase())));

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<MessageResponse> signoutUser() {
        SecurityContextHolder.clearContext();
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    public boolean isAuthenticated(HttpServletRequest request) {
        return jwtUtils.getJwtFromCookies(request)
                .map(jwtUtils::validateJwtToken)
                .orElse(false);
    }

    public ResponseEntity<Object> getUserDetails(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<String> roles = extractRoles(userDetails);

        UserResponce response = new UserResponce(
                userDetails.getId(),
                userDetails.getUsername(),
                roles
        );

        return ResponseEntity.ok(response);
    }

    private Authentication performAuthentication(CheckRequest loginRequest) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    ));
        } catch (AuthenticationException exception) {
            throw new BadCredentialsException("Error: Invalid credentials provided.");
        }
    }

    private Role getOrCreateRole(AppRole appRole) {
        // Role'ü kontrol et, yoksa oluştur ve kaydet
        return roleRepository.findByRoleName(appRole)
                .orElseGet(() -> roleRepository.save(new Role(appRole)));
    }

    private List<String> extractRoles(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
