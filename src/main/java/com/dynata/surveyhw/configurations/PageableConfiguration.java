package com.dynata.surveyhw.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfiguration {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return resolver -> resolver.setOneIndexedParameters(true);
    }
}
