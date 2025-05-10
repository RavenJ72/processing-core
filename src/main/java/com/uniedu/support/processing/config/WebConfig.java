package com.uniedu.support.processing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Разрешить все пути
                .allowedOrigins("*") // Разрешить доступ с любого хоста
                .allowedMethods("*"); // Разрешить все HTTP-методы
    }
}