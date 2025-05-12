package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.Role;
import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    List<User> findAllByIsActive(WorkerStatus status);

    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.role = :roleType")
    List<User> findByRoleType(@Param("roleType") UserRoleType roleType);
}

