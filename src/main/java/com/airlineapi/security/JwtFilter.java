package com.airlineapi.security;

import com.airlineapi.security.JwtUtils;
import com.airlineapi.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // JWT'yi çerezlerden al
        Optional<String> tokenOptional = jwtUtils.getJwtFromCookies(request);

        try {
            if (tokenOptional.isPresent()) {
                String token = tokenOptional.get();

                // JWT'yi doğrula
                if (jwtUtils.validateJwtToken(token)) {
                    // Kullanıcı adını (email) JWT'den al
                    String username = jwtUtils.getEmailFromJwtToken(token);

                    // Kullanıcı bilgilerini yükle
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Kimlik doğrulama token oluştur
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // SecurityContext'e kimlik doğrulamayı ekle
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired JWT token: " + e.getMessage());
            return;
        }

        // Bir sonraki filtreye geç
        filterChain.doFilter(request, response);
    }
}
