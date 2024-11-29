package com.airlineapi.service;

import com.airlineapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kullanıcıyı veritabanından bul
        return userRepository.findByUsername(username)
                .map(user -> {
                    // UserDetails nesnesine dönüştür
                    return User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword()) // Şifre hashlenmiş şekilde gelir
                            .roles(user.getRoles().stream()
                                    .map(role -> role.getRoleName().name()) // Rolleri büyük harf yap
                                    .toArray(String[]::new))
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));
    }
}
