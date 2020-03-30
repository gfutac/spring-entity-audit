package com.gfutac.model.repositories;

import com.gfutac.model.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEntityRepository extends JpaRepository<AuditEntity, Long> {
}
