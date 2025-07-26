package com.alura.literalura.repositories;

import com.alura.literalura.entities.LiterAluraAuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface LiterAluraAuthorRepository extends JpaRepository<LiterAluraAuthorEntity, Long> {
        Optional<LiterAluraAuthorEntity> findByName(String name);

        @Query("SELECT a FROM LiterAluraAuthorEntity a LEFT JOIN FETCH a.books")
        List<LiterAluraAuthorEntity> findAllWithBooks();

        @Query("SELECT a FROM LiterAluraAuthorEntity a " +
                "WHERE (a.birthYear <= :year) AND " +
                "(a.deathYear IS NULL OR a.deathYear >= :year) " +
                "ORDER BY a.name")
        List<LiterAluraAuthorEntity> findAliveInYear(@Param("year") int year);
}

