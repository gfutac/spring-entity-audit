package com.gfutac.jms;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

@Component
public class AuditTopic extends ActiveMQTopic {

    private JmsTemplate jmsTemplate;

    public AuditTopic(@Value("${topics.audit.name}") final String topicName, @Autowired final JmsTemplate jmsTemplate) {
        super(topicName);
        this.jmsTemplate = jmsTemplate;
    }

    public void send(Object message) {
        this.jmsTemplate.convertAndSend(this, message);
    }
}
