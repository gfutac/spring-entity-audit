package com.gfutac.audit;

import com.fasterxml.jackson.annotation.JsonFilter;


@JsonFilter(AuditEntityColumnPropertyFilterConfiguration.entityColumnFilterName)
public interface AuditableEntity {
}

