package com.airlineapi.service;

import com.airlineapi.dto.RoleDTO;
import com.airlineapi.model.Role;
import com.airlineapi.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        Role savedRole = rolesRepository.save(role);
        return modelMapper.map(savedRole, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return rolesRepository.findAll().stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }
}
