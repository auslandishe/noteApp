package com.example.noteappbackend.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import java.util.Objects;

@Entity
@Table(name = "sticky_note") // 建議明確指定資料表名稱
public class StickyNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(nullable = false, length = 20) // 新增顏色欄位長度限制
    private String color;

    @Column(nullable = false)
    private Integer position_x;

    @Column(nullable = false)
    private Integer position_y;


    @ManyToOne(optional = false) // 筆記必須有使用者
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // 新增級聯操作
    @JoinTable(name = "sticky_note_tag",
            joinColumns = @JoinColumn(name = "sticky_note_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    // 為了 JPA 規範需要一個無參數的建構子
    public StickyNote() {
    }

    public StickyNote(String title, String content, String color) {
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public StickyNote(Long id, String title, String content, String color) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public StickyNote(Long id, String title, String content, String color, Integer position_x, Integer position_y, User user, Set<Tag> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.color = color;
        this.position_x = position_x;
        this.position_y = position_y;
        this.user = user;
        this.tags = tags;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPosition_x() {
        return position_x;
    }

    public void setPosition_x(Integer position_x) {
        this.position_x = position_x;
    }

    public Integer getPosition_y() {
        return position_y;
    }

    public void setPosition_y(Integer position_y) {
        this.position_y = position_y;
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

    @Override
    public String toString() {
        return "StickyNote{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", color='" + color + '\'' +
                ", position_x=" + position_x +
                ", position_y=" + position_y +
                ", user=" + user +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StickyNote that = (StickyNote) o;
        // 使用 id 進行比較，效率更高且更可靠
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // 使用 id 進行雜湊，效率更高且更可靠
        return Objects.hash(id);
    }
}

