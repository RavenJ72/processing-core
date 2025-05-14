package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.dto.base.BaseDTO;
import com.uniedu.support.processing.models.baseEntities.BaseEntity;
import com.uniedu.support.processing.models.entities.ChatMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
public class ChatDto extends BaseDTO {
    List<ChatMessage> messages = new ArrayList<>();
}
