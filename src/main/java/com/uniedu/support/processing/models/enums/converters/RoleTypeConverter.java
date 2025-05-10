package com.uniedu.support.processing.models.enums.converters;

import com.uniedu.support.processing.models.enums.UserRoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<UserRoleType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRoleType attribute) {
        return attribute == null ? null : attribute.getUserRoleTypeCode();
    }

    @Override
    public UserRoleType convertToEntityAttribute(Integer dbData) {
        for (UserRoleType roleType : UserRoleType.values()) {
            if (roleType.getUserRoleTypeCode() == dbData) {
                return roleType;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
