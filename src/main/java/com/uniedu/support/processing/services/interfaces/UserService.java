package com.uniedu.support.processing.services;

import com.uniedu.support.processing.dto.standart.UserDTO;
import com.uniedu.support.processing.models.enums.UserRoleType;

import java.util.List;

public interface UserService<ID> {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(UserDTO userDTO);
    UserDTO getUserById(ID id);
    UserDTO getUserByEmail(String email);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();

    List<UserDTO> findAllByRoleAsc(UserRoleType role);
}