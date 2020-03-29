package com.gfutac.services;

import com.gfutac.Application;
import com.gfutac.audit.model.EntityStateChangeType;
import com.gfutac.model.AuditEntity;
import com.gfutac.model.Author;
import com.gfutac.model.repositories.AuditEntityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

@Profile("rdbms")
@ActiveProfiles("rdbms")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)

public class RdbTest {

    @Autowired
    private AuditEntityRepository auditEntityRepository;

    @Test
    public void test() {
        var ent = new AuditEntity();
        ent.setEntityType(Author.class.getName());
        ent.setEntityStateChangeTime(Instant.now());
        ent.setEntityStateChangeType(EntityStateChangeType.INSERT);
        ent.setEntity("some entity");
        ent.setEntityKey("32");

        this.auditEntityRepository.saveAndFlush(ent);
    }

}
