package com.gfutac.audit;

import com.gfutac.service.AuditService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditableRepositoryAspect {

    @Autowired
    private AuditService auditService;

    @Pointcut("execution(* org.springframework.data.repository.Repository+.save(..)) || " +
            "execution(* org.springframework.data.repository.Repository+.saveAll(..)) || " +
            "execution(* org.springframework.data.repository.Repository+.saveAndFlush(..))")
    public void save() {}

    @Around("save()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        Object resultOfSave = jp.proceed();
        this.auditService.auditSavedObject(resultOfSave);
        return resultOfSave;
    }
}
