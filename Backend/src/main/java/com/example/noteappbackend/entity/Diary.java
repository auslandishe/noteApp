package com.example.noteappbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "diary") // 建議明確指定資料表名稱
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100) // 新增欄位限制，確保資料完整性
    private String title;

    @Column(nullable = false, length = 50)
    private String author;

    @Lob
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false) // user_id 欄位不可為空
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // 新增級聯操作
    @JoinTable(name = "diary_tag",
            joinColumns = @JoinColumn(name = "diary_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();


    // JPA 規範需要一個無參數的建構子
    public Diary() {
    }

    public Diary(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Diary(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Diary(String title, Long id, String author, String content) {
        this.title = title;
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public Diary(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    // 建議新增一個沒有 ID 的建構子，讓 JPA 更好操作
    public Diary(String title, String content, User user, Set<Tag> tags) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.tags = tags;
    }

    public Diary(Long id, String title, String author, String content, LocalDateTime createdAt, LocalDateTime updatedAt, User user, Set<Tag> tags) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.tags = tags;
    }

    // 在新增資料前，自動設定 createdAt 和 updatedAt
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 在更新資料前，自動設定 updatedAt
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 以下為 getters 和 setters (省略部分，但應該全部保留)
    public Long getId() {
        return id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    // 覆寫 toString() 方法，方便除錯
    @Override
    public String toString() {
        return "Diary{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diary diary = (Diary) o;
        // 使用 id 進行比較，效率更高且更可靠
        return Objects.equals(id, diary.id);
    }

    @Override
    public int hashCode() {
        // 使用 id 進行雜湊，效率更高且更可靠
        return Objects.hash(id);
    }
}

