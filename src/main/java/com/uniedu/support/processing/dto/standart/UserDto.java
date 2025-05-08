package sasdevs.backend.dto.standard;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import sasdevs.backend.dto.base.TimestampedDTO;
import sasdevs.backend.models.entities.Group;
import sasdevs.backend.models.entities.Role;
import sasdevs.backend.validators.user.UniqueEmail;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDTO extends TimestampedDTO{
    @NotNull(message = "First name cannot be null!")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "First name must start with a capital letter and contain only letters!")
    private String firstName;

    @NotNull(message = "Last name cannot be null!")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Last name must start with a capital letter and contain only letters!")
    private String lastName;

    @UniqueEmail
    @NotNull(message = "Email cannot be null!")
    @Pattern(regexp = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$", message = "Email must be in the format text@text.text!")
    private String email;

    //TODO: add validation for pass
    @NotNull(message = "Password cannot be null!")
    private String password;
    private String imageUrl;
    private Boolean isActive;
    private Set<Role> roles = new HashSet<>();
    private GroupDTO group;
}