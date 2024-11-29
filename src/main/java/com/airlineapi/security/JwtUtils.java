package com.airlineapi.security;

import com.airlineapi.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final String jwtSecret = "553f5845645729683e4343552a2d4950773c2d425142674b204e707d3chep";
    private final int jwtExpirationMs = 100000; // Token süresi (milisaniye)
    private final String jwtCookieName = "JwtCookie";


    public String generateToken(String username) {
        int jwtExpirationMs = 100000;
        return Jwts.builder()
                .setSubject(username) // Kullanıcı adı veya e-posta
                .setIssuedAt(new Date()) // Token oluşturulma zamanı
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Token geçerlilik süresi
                .signWith(key(), SignatureAlgorithm.HS256) // İmzalama için secret key
                .compact();
    }


    public Optional<String> getJwtFromCookies(HttpServletRequest request) {
        String jwtCookieName = "JwtCookie";
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        return cookie != null ? Optional.of(cookie.getValue()) : Optional.empty();
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
        String jwt = generateToken(userDetails.getUsername());

        return ResponseCookie.from(jwtCookieName, jwt)
                .path("/") // Cookie'nin kullanılabileceği path
                .maxAge(jwtExpirationMs / 1000) // Cookie'nin geçerlilik süresi (saniye)
                .httpOnly(true) // HTTPOnly güvenlik bayrağı
                .secure(true) // HTTPS kullanıyorsanız secure bayrağı
                .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName, null)
                .path("/") // Çerezin hangi yollarda geçerli olacağı
                .httpOnly(true) // Çerezin sadece HTTP üzerinden erişilebilir olması
                .maxAge(0) // Çerezin hemen geçersiz kılınması için süresi sıfırlanır
                .build();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> getUserRolesFromJwtToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (List<String>) claims.get("roles");
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("JWT token validation error: {}", e.getMessage());
        }
        return false;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}
