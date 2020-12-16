package com.gfutac.audit.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class EntityColumnSerializer extends SimpleBeanPropertyFilter {

    @Autowired
    private EntityIdMetadataService entityIdMetadataService;

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {

        if (this.include(writer)) {
            if (writer.findAnnotation(javax.persistence.Column.class) != null) {
                writer.serializeAsField(pojo, jgen, provider);
            } else if (writer.findAnnotation(javax.persistence.JoinColumn.class) != null) {
                // JoinColumn's will be serialized as key:value pairs
                // where key is name of the referenced foreign key and value is value of it,
                // meaning, whole object (referenced entity) will not be serialized, only it's primary key
                if (writer instanceof BeanPropertyWriter) {
                    var beanPropertyWriter = (BeanPropertyWriter) writer;
                    var entity = beanPropertyWriter.get(pojo);

                    if (entity != null) {
                        var metadata = entityIdMetadataService.extractEntityIdMetadata(entity, beanPropertyWriter);

                        var key = metadata.getKeyName();
                        var value = metadata.getKeyValue();

                        // simplification - if it is a number try to write it as Long or BigDecimal
                        if (value instanceof Number) {
                            if (value instanceof Float || value instanceof Double || value instanceof BigDecimal) {
                                jgen.writeNumberField(key, BigDecimal.valueOf(Double.parseDouble(value.toString())));
                            } else {
                                jgen.writeNumberField(key, Long.parseLong(value.toString()));
                            }
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
}
