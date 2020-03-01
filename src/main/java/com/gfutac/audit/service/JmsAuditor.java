package com.gfutac.audit.service;

import com.gfutac.jms.AuditTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsAuditor implements Auditor {

    @Autowired
    private AuditTopic auditTopic;

    @Override
    public void audit(Object message) {
        this.auditTopic.send(message);
    }
}
