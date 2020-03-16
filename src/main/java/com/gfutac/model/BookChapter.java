package com.gfutac.model;

import com.gfutac.audit.model.AuditableEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@AuditableEntity
@Table(name = "BookChapter")

@Data
@Accessors(chain = true)
@ToString
public class BookChapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookChapterID")
    private long bookChapterId;

    @Column(name = "ChapterName")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId")
    @ToString.Exclude
    private Book book;
}
