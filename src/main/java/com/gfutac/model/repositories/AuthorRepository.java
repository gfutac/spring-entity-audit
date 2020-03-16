package com.gfutac.model.repositories;

import com.gfutac.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByName(String authorName);
}
