package com.gfutac.jms;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditTopic extends ActiveMQTopic {

    private JmsTemplate jmsTemplate;

    public AuditTopic(@Value("${auditor.topic.name}") final String topicName, @Autowired final JmsTemplate jmsTemplate) {
        super(topicName);
        this.jmsTemplate = jmsTemplate;
    }

    public void send(Object message) {
        try {
            this.jmsTemplate.convertAndSend(this, message);
        } catch (JmsException e) {
            log.error("Message {} was not published due to an error: {}", message, e);
        }
    }
}
