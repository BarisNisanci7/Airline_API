package com.airlineapi.service;

import com.airlineapi.dto.UserDTO;
import com.airlineapi.model.AppRole;
import com.airlineapi.model.Role;
import com.airlineapi.model.User;
import com.airlineapi.repository.RolesRepository;
import com.airlineapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    public UserDTO createUser(UserDTO userDTO) {

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + userDTO.getUsername());
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());

        userDTO.getRoles().forEach(roleName -> {
            // Önce Role nesnesini kaydet veya veritabanında mevcutsa al
            Role role = rolesRepository.findByRoleName(AppRole.valueOf(String.valueOf(roleName)))
                    .orElseGet(() -> {
                        Role newRole = new Role(AppRole.valueOf(String.valueOf(roleName)));
                        return rolesRepository.save(newRole); // Role kaydedilir
                    });
            user.addRole(role);
        });

        userRepository.save(user);
        return userDTO;
    }



    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }
}
