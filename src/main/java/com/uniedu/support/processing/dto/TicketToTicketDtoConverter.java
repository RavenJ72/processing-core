package com.uniedu.support.processing.dto;

import com.uniedu.support.processing.dto.standart.ChatDto;
import com.uniedu.support.processing.dto.standart.RoomDto;
import com.uniedu.support.processing.dto.standart.TicketDto;
import com.uniedu.support.processing.dto.standart.UserDto;
import com.uniedu.support.processing.models.entities.Ticket;
import org.modelmapper.ModelMapper;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;


public class TicketToTicketDtoConverter implements Converter<Ticket, TicketDto> {

    private final ModelMapper modelMapper;

    public TicketToTicketDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TicketDto convert(MappingContext<Ticket, TicketDto> context) {
        Ticket ticket = context.getSource();

        // Преобразование Ticket в TicketDto
        TicketDto ticketDto = new TicketDto();
        ticketDto.setTitle(ticket.getTitle());
        ticketDto.setDescription(ticket.getDescription());
        ticketDto.setStatus(ticket.getStatus());

        // Преобразование Room, Chat, и пользователей с использованием ModelMapper
        ticketDto.setRoom(modelMapper.map(ticket.getRoom(), RoomDto.class));
        ticketDto.setChat(modelMapper.map(ticket.getChat(), ChatDto.class));
        ticketDto.setCreator(modelMapper.map(ticket.getCreator(), UserDto.class));
        ticketDto.setAssignedTo(modelMapper.map(ticket.getAssignedTo(), UserDto.class));

        return ticketDto;
    }
}
