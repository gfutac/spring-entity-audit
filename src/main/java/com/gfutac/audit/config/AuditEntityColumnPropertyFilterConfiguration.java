package com.gfutac.audit.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditEntityColumnPropertyFilterConfiguration {

    @Autowired
    private MetaModelService metaModelService;

    @Bean
    public PropertyFilter entityColumnFilter() {
        return new SimpleBeanPropertyFilter() {

            @Override
            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {

                if (this.include(writer)) {
                    if (writer.findAnnotation(javax.persistence.Column.class) != null) {
                        writer.serializeAsField(pojo, jgen, provider);
                    } else if (writer.findAnnotation(javax.persistence.JoinColumn.class) != null) {
                        if (writer instanceof BeanPropertyWriter) {
                            var beanPropertyWriter = (BeanPropertyWriter)writer;
                            var entity = beanPropertyWriter.get(pojo);

                            if (entity != null) {
                                var key = metaModelService.getEntityKeyName(beanPropertyWriter.getType().getRawClass());
                                var value = metaModelService.getEntityKeyValue(entity);

                                if (value instanceof Number) {
                                    jgen.writeNumberField(key, Long.parseLong(value.toString()));
                                } else {
                                    jgen.writeStringField(key, value.toString());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            protected boolean include(BeanPropertyWriter writer) {
                return true;
            }

            @Override
            protected boolean include(PropertyWriter writer) {
                return true;
            }
        };
    }
}
