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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Component
public class EntityUpdatedEventListener implements PostUpdateEventListener, PostInsertEventListener {

    private Set<Class<?>> auditableEntities;

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

        this.auditableEntities = new HashSet<>();
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        this.doAudit(event.getEntity(), event.getId(), EntityStateChangeType.INSERT);
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        this.doAudit(event.getEntity(), event.getId(), EntityStateChangeType.UPDATE);
    }

    private void doAudit(Object entity, Serializable entityKey, EntityStateChangeType changeType) {
        if (!this.auditableEntities.contains(entity.getClass())) {
            for (var annotation : entity.getClass().getAnnotations()) {
                if (annotation.annotationType().equals(AuditableEntity.class)) {
                    this.auditableEntities.add(entity.getClass());
                    break;
                }
            }
        }

        var isEntityAuditable = this.auditableEntities.contains(entity.getClass());

        if (isEntityAuditable) {
            this.auditService.auditChangedEntity(entity, entityKey, changeType);
        }
    }
}
