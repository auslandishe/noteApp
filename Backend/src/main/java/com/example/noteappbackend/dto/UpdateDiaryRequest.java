package com.example.noteappbackend.dto;

import java.util.List;
import java.util.Objects;

public class UpdateDiaryRequest {
    private String title;
    private String author;
    private String content;
    private List<String> tagNames;

    public UpdateDiaryRequest() {
    }

    public UpdateDiaryRequest(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public UpdateDiaryRequest(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public UpdateDiaryRequest(String title, String author, String content, List<String> tagNames) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.tagNames = tagNames;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateDiaryRequest that = (UpdateDiaryRequest) o;
        return Objects.equals(title, that.title) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }
}

