package com.gfutac.model.repositories;

import com.gfutac.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> getAllByAuthor_Name(String authorName);

    boolean existsByName(String name);
}
