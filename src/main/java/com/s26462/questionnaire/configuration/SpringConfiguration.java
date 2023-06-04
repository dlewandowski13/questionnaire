package com.s26462.questionnaire.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * The type Spring configuration.
 */
@SpringBootConfiguration
public class SpringConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}