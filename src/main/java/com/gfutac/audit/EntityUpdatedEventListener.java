package com.gfutac.audit;

import com.gfutac.service.AuditService;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Component
public class EntityUpdatedEventListener implements PostUpdateEventListener {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private AuditService auditService;

    @PostConstruct
    private void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(this);
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        var updatedEntity = event.getEntity();
        var isEntityAuditable = false;

        for (var annotation : updatedEntity.getClass().getAnnotations()) {
            if (annotation.annotationType().equals(AuditableEntity.class)) {
                isEntityAuditable = true;
                break;
            }
        }

        if (isEntityAuditable) {
            this.auditService.auditSavedObject(updatedEntity);
        }
    }
}
