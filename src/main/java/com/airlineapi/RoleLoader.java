package com.airlineapi;

import com.airlineapi.repository.RolesRepository;
import com.airlineapi.model.Role;
import com.airlineapi.model.AppRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleLoader implements CommandLineRunner {

    private final RolesRepository roleRepository;

    public RoleLoader(RolesRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        Role adminRole = new Role();
        adminRole.setRoleName(AppRole.ROLE_ADMIN);
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName(AppRole.ROLE_USER);
        roleRepository.save(userRole);
    }
}
