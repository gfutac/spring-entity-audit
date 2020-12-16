package com.gfutac.audit.config;

import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import lombok.Data;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Data
@Service
public class EntityIdMetadataService {

    @Autowired
    private EntityManagerFactory emf;

    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        this.sessionFactory = emf.unwrap(SessionFactoryImpl.class);
    }

    public EntityIdMetadata extractEntityIdMetadata(Object entity, BeanPropertyWriter beanPropertyWriter) {
        var result = new EntityIdMetadata();

        // this is executed in new thread (auditing is done in new thread, we don't want to block main thread)
        // so we need entity manager injected here because sessions are not shared across threads.
        var entityMetaModel = sessionFactory.getMetamodel().entity(beanPropertyWriter.getType().getRawClass());
        var keyType = entityMetaModel.getIdType().getJavaType();

        var key = entityMetaModel.getDeclaredId(keyType).getName();
        // entity is sometimes hibernate proxy and we need to unproxy it. session is needed for that
        var value = emf.getPersistenceUnitUtil().getIdentifier(entity);

        result.setKeyName(key);
        result.setKeyValue(value);

        return result;
    }
}
