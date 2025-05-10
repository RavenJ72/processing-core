package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.Role;
import com.uniedu.support.processing.models.enums.UserRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(UserRoleType role);
}
