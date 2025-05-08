package sasdevs.backend.models.entities;

import jakarta.persistence.*;
import lombok.*;

import sasdevs.backend.models.enums.converters.RoleTypeConverter;
import sasdevs.backend.models.enums.UserRoleType;
import sasdevs.backend.models.baseEntities.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    @Convert(converter = RoleTypeConverter.class)
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role", length = 9, nullable = false)
    private UserRoleType role;
}
