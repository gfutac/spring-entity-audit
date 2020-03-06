package com.gfutac.audit.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AuditorFactoryBean implements FactoryBean<Auditor> {

    private Class<?> auditorType;

    @Autowired
    private ApplicationContext applicationContext;

    public AuditorFactoryBean(@Value("${auditor.type}") Class<?> auditorType) {
        super();
        this.auditorType = auditorType;
    }

    @Override
    public Auditor getObject() {
        return (Auditor) this.applicationContext.getBean(this.auditorType);
    }

    @Override
    public Class<?> getObjectType() {
        return Auditor.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
