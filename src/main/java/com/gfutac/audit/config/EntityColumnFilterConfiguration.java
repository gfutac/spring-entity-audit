package com.gfutac.audit.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Column;
import javax.persistence.Id;

@Configuration
public class EntityColumnFilterConfiguration {

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
                                var metadata = getIdMetadata(entity);

                                var key = metadata.getKeyName();
                                var value = metadata.getKeyValue();

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

    private EntityIdMetadata getIdMetadata(Object entity) throws IllegalAccessException {
        var type = entity.getClass();
        var declaredFields = type.getDeclaredFields();

        var result = new EntityIdMetadata();

        for (var field : declaredFields) {
            if (field.isAnnotationPresent(Column.class) && field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                result.setKeyName(field.getName());
                result.setKeyValue(field.get(entity));
            }
        }

        return result;
    }

    @Data
    @Accessors(chain = true)
    private static class EntityIdMetadata {
        private String keyName;
        private Object keyValue;
    }
}
