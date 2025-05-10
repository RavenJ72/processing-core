package com.uniedu.support.processing.models.entities;

import com.uniedu.support.processing.models.baseEntities.BaseEntity;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.models.enums.converters.RoleTypeConverter;
import jakarta.persistence.*;
import lombok.*;


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
