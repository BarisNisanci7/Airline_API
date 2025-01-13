package com.airlineapi.repository;

import com.airlineapi.model.Role;
import com.airlineapi.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(AppRole name);

}
