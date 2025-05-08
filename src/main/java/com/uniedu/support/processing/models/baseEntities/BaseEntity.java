package sasdevs.backend.models.baseEntities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

@NoArgsConstructor
@Getter
@Setter

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid-string")
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id", updatable = false, nullable = false)
//    @GenericGenerator(name = "uuid-string",
//            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
}
