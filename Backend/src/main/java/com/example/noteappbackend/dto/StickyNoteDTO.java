package com.example.noteappbackend.dto;
import java.util.List;
import java.util.Objects;

public class StickyNoteDTO {
    private Long id;
    private String title;
    private String content;
    private int positionX;
    private int positionY;
    private String color;
    private Long userId;
    private List<String> tags;
    private String createdAt;
    private String updatedAt;

    public StickyNoteDTO() {
    }


    public StickyNoteDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public StickyNoteDTO(String title, String content, String color) {
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public StickyNoteDTO(Long id, String title, String content, String color) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public StickyNoteDTO(Long id, String title, String content, int positionX, int positionY, String color, Long userId, List<String> tags, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.positionX = positionX;
        this.positionY = positionY;
        this.color = color;
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

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    @Override
    public String toString() {
        return "StickyNoteDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                ", color='" + color + '\'' +
                ", userId=" + userId +
                ", tags=" + tags +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StickyNoteDTO that = (StickyNoteDTO) o;
        return Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, color);
    }
}

