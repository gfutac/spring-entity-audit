package com.gfutac.audit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncAuditConfig {

    @Bean(name = "auditThreadPool")
    public Executor auditThreadPool(@Value("${auditor.async.threads}") Integer threads) {
        threads = threads == null ? 1 : threads;
        var pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(threads);

        return pool;
    }
}
