package com.example.noteappbackend.dto;

import java.util.List;

public class CreateDiaryRequest {
    private String title;
    private String content;
    private String author;
    private List<String> tagNames;

    public CreateDiaryRequest() {
    }

    public CreateDiaryRequest(String title, String content, String author, List<String> tagNames) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.tagNames = tagNames;
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

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

