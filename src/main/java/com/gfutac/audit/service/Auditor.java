package com.gfutac.audit.service;

import com.gfutac.audit.model.AuditEntity;

public interface Auditor {
    void audit(AuditEntity message);
}
