package com.gfutac.audit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.gfutac.audit.model.AuditableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityWriterConfiguration {

    private static final String entityColumnFilterName = "ENTITY_COLUMN_FILTER";

    /**
     * ObjectWriter that will be used for serializing saved Hibernate entities annotated with @{@link AuditableEntity}
     * @param entityColumnFilter Injected PropertyFilter from {@link EntityColumnFilterConfiguration}
     * @return customized {@link ObjectWriter}
     */
    @Bean
    public ObjectWriter entityWriter(@Autowired PropertyFilter entityColumnFilter) {
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Customized annotation inspector. By default it tries to find all classes
        // annotated with @JsonFilter("name-of-filter"), but goal here was to
        // use only @AuditableEntity annotation on classes that we want to audit
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public Object findFilterId(Annotated a) {
                AuditableEntity ann = this._findAnnotation(a, AuditableEntity.class);
                if (ann != null) {
                    return entityColumnFilterName;
                }

                return null;
            }
        });

        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter(entityColumnFilterName, entityColumnFilter);
        return mapper.writer(filters);
    }
}
