package com.example.noteappbackend.dto;

import java.util.List;

public class DiaryDTO {
    private Long id;
    private String title;
    private String author;
    private String content;
    private Long userId;
    private List<String> tags;
    private String createdAt;
    private String updatedAt;

    public DiaryDTO() {
    }

    public DiaryDTO(Long id, String title, String author, String content, Long userId, List<String> tags, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.userId = userId;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

