package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.dto.base.BaseDTO;
import com.uniedu.support.processing.models.enums.RoomGroup;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto extends BaseDTO {

    private String name;

    private RoomGroup roomGroup;
}
