package com.airlineapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

@Configuration
public class ModelMapperConfig {

    private final ModelMapper modelMapper;

    // Constructor injection
    public ModelMapperConfig() {
        this.modelMapper = new ModelMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        return this.modelMapper;
    }
}
