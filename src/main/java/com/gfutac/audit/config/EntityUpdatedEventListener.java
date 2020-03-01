package com.gfutac.audit.config;

import com.gfutac.audit.model.AuditableEntity;
import com.gfutac.audit.model.EntityStateChangeType;
import com.gfutac.audit.service.AuditService;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Component
public class EntityUpdatedEventListener implements PostUpdateEventListener, PostInsertEventListener {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private AuditService auditService;

    @PostConstruct
    private void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(this);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(this);
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        var insertedEntity = event.getEntity();
        this.doAudit(insertedEntity, EntityStateChangeType.INSERT);
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        var updatedEntity = event.getEntity();
        this.doAudit(updatedEntity, EntityStateChangeType.UPDATE);
    }

    private void doAudit(Object entity, EntityStateChangeType changeType) {
        var isEntityAuditable = false;

        for (var annotation : entity.getClass().getAnnotations()) {
            if (annotation.annotationType().equals(AuditableEntity.class)) {
                isEntityAuditable = true;
                break;
            }
        }

        if (isEntityAuditable) {
            this.auditService.auditChangedEntity(entity, changeType);
        }
    }
}
