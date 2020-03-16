package com.gfutac.audit.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;

@Configuration
public class EntityColumnFilterConfiguration {

    @Autowired
    private EntityManagerFactory emf;

    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        this.sessionFactory = emf.unwrap(SessionFactoryImpl.class);
    }

    @Bean
    public PropertyFilter entityColumnFilter() {
        return new SimpleBeanPropertyFilter() {

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
                                var metadata = getIdMetadata(entity, beanPropertyWriter);

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
        };
    }

    private EntityIdMetadata getIdMetadata(Object entity, BeanPropertyWriter beanPropertyWriter) {
        var result = new EntityIdMetadata();

        // this is executed in new thread (auditing is done in new thread, we don't want to block main thread)
        // so we need entity manager injected here because sessions are  not shared across threads.
        var entityMetaModel = sessionFactory.getMetamodel().entity(beanPropertyWriter.getType().getRawClass());
        var keyType = entityMetaModel.getIdType().getJavaType();

        var key = entityMetaModel.getDeclaredId(keyType).getName();
        // entity is sometimes hibernate proxy and we need to unproxy it. session is needed for that
        var value = emf.getPersistenceUnitUtil().getIdentifier(entity);

        result.setKeyName(key);
        result.setKeyValue(value);

        return result;
    }

    @Data
    private static class EntityIdMetadata {
        private String keyName;
        private Object keyValue;
    }
}
