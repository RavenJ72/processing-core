package sasdevs.backend.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoleType {
    STUDENT(1),
    TEACHER(2),
    ADMIN(3);


    private final int userRoleTypeCode;
}

