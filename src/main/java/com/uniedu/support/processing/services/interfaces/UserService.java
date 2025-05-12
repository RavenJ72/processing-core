package com.uniedu.support.processing.services.interfaces;

import com.uniedu.support.processing.dto.authEntities.SignUpRequest;
import com.uniedu.support.processing.dto.standart.UserDto;
import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.enums.UserRoleType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService<ID> {
    UserDto createUser(SignUpRequest userDTO, UserDetails userDetails);
    UserDto updateUser(UserDto userDTO);
    UserDto getUserById(ID id);
    UserDto getUserByEmail(String email);
    UserDto getUserByUsername(String username);

    List<UserDto> findAllByRoleAsc(UserRoleType role);
    User getUserForTicketAssigmentByRoomName(String roomName);
}