package com.gfutac.model;

import com.gfutac.audit.model.AuditableEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@AuditableEntity
@Table(name = "Book")

@Data
@Accessors(chain = true)
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookID")
    private long bookId;

    @Column(name = "Name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    @ToString.Exclude
    private Author author;
}
