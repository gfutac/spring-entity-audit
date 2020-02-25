package com.gfutac.entities;

import com.gfutac.audit.AuditableEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@AuditableEntity
@Table(name = "Book")
@Data
@Accessors(chain = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookID")
    private long bookId;

    @Column(name = "Name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private Author author;
}
