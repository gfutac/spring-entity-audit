# spring-entity-audit

Simple project to investigate how to handle Hibernate's events in order to log changes of affected entities.
Changes are published to ActiveMQ topic `audit-topic`.

Dockerized ActiveMQ was used: https://hub.docker.com/r/rmohr/activemq/

# Starting ActiveMQ
`docker pull rmohr/activemq`
<br>
`docker run -p 61616:61616 -p 8161:8161 rmohr/activemq`

# Running samples
There are few test that can be executed
