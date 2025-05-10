package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    List<User> findAllByIsActive(WorkerStatus status);
}
