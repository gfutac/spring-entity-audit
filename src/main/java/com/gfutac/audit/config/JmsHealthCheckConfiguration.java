package com.gfutac.audit.config;

import org.apache.activemq.transport.TransportListener;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQConnectionFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JmsHealthCheckConfiguration {
    @Bean
    public TransportListener transportListener() {
        return new TransportListener() {
            @Override
            public void onCommand(Object command) {
                var x = 0;
            }

            @Override
            public void onException(IOException error) {
                var x = 0;
            }

            @Override
            public void transportInterupted() {
                var x = 0;
            }

            @Override
            public void transportResumed() {
                var x = 0;
            }
        };
    }

    @Bean
    public ActiveMQConnectionFactoryCustomizer customActiveMQConnectionFactoryCustomizer() {
        return factory -> factory.setTransportListener(transportListener());
    }
}
