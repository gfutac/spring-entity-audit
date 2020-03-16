package com.gfutac.audit.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Entities annotated with @{@link AuditableEntity} will be processed after save/update.
 * {@link EntityUpdatedEventListener} checks if saved/updated entity is annotated with @{@link AuditableEntity}
 * and audits it. Customized ObjectWriter (@{@link com.gfutac.audit.config.EntityWriterConfiguration}) and @{@link com.gfutac.audit.config.EntityColumnFilterConfiguration}
 * also rely on @{@link AuditableEntity}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditableEntity {
}
