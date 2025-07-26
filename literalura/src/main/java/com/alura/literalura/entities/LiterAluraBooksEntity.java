package com.alura.literalura.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class LiterAluraBooksEntity {
    @Id
    private Long id;

    private String title;
    private String language;
    private Integer downloadCount;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private LiterAluraAuthorEntity author;

    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public LiterAluraAuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(LiterAluraAuthorEntity author) {
        this.author = author;
    }
}

