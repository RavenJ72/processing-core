package com.uniedu.support.processing.config;

import com.uniedu.support.processing.dto.TicketToTicketDtoConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setAmbiguityIgnored(true);

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        // Добавление кастомного конвертера для Ticket -> TicketDto
        modelMapper.addConverter(new TicketToTicketDtoConverter(modelMapper));

        return modelMapper;
    }
}
