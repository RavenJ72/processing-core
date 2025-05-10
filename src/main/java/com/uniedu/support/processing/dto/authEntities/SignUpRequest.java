package com.uniedu.support.processing.dto.authEntities;

import com.uniedu.support.processing.models.enums.UserRoleType;
import lombok.Data;

@Data
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private UserRoleType userRole;
}