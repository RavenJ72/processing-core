package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.dto.base.TimestampedDTO;
import com.uniedu.support.processing.models.entities.Role;
import com.uniedu.support.processing.models.entities.Room;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto extends TimestampedDTO {
//    @NotNull(message = "First name cannot be null!")
//    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "First name must start with a capital letter and contain only letters!")
    private String firstName;

//    @NotNull(message = "Last name cannot be null!")
//    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Last name must start with a capital letter and contain only letters!")
    private String lastName;

//    @NotNull(message = "Email cannot be null!")
//    @Pattern(regexp = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$", message = "Email must be in the format text@text.text!")
    private String email;
    private String username;
    private String phoneNumber;

    @NotNull(message = "Password cannot be null!")
    private String password;
    private String imageUrl;
    private WorkerStatus isActive;
    private Set<Role> roles = new HashSet<>();
    private Set<Room> assignedRooms = new HashSet<>();
}