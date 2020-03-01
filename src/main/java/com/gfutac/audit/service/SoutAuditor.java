package com.gfutac.audit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SoutAuditor implements Auditor {
    @Override
    public void audit(Object message) {
        log.info("" + message);
    }
}
