package sasdevs.backend.dto.base;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TimestampedDTO extends BaseDTO {
    private LocalDateTime created;
    private LocalDateTime modified;
}
