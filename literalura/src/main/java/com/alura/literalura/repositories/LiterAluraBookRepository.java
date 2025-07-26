package com.alura.literalura.repositories;

import com.alura.literalura.entities.LiterAluraBooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LiterAluraBookRepository extends JpaRepository<LiterAluraBooksEntity, Long> {
    @Query("SELECT DISTINCT b.language FROM LiterAluraBooksEntity b ORDER BY b.language")
    List<String> findDistinctLanguages();

    @Query("SELECT b FROM LiterAluraBooksEntity b JOIN FETCH b.author WHERE LOWER(b.language) = LOWER(:language)")
    List<LiterAluraBooksEntity> findByLanguageIgnoreCase(@Param("language") String language);
}