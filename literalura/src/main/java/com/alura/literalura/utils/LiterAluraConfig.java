package com.alura.literalura.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LiterAluraConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
