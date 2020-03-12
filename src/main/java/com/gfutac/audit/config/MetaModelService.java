package com.gfutac.audit.config;

import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Service
public class MetaModelService {

    @Autowired
    private EntityManagerFactory emf;

    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        // SessionFactory is Hibernate specific
        this.sessionFactory = emf.unwrap(SessionFactoryImpl.class);
    }

    public String getEntityKeyName(Class<?> type) {
        var entityMetaModel = sessionFactory.getMetamodel().entity(type);
        var keyType = entityMetaModel.getIdType().getJavaType();

        return entityMetaModel.getDeclaredId(keyType).getName();
    }

    public Object getEntityKeyValue(Object entity) {
        return emf.getPersistenceUnitUtil().getIdentifier(entity);
    }
}
