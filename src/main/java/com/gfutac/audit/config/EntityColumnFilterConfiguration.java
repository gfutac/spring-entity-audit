package com.gfutac.audit.config;

import com.fasterxml.jackson.databind.ser.PropertyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityColumnFilterConfiguration {

    @Bean
    public PropertyFilter entityColumnFilter() {
        return new EntityColumnSerializer();
    }
}
