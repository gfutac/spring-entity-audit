package com.gfutac.repositories;

import com.gfutac.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByName(String authorName);
}
