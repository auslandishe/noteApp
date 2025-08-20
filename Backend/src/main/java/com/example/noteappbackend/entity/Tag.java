package com.example.noteappbackend.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @ManyToMany(mappedBy = "tags")
    private Set<Diary> diaries = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    private Set<StickyNote> stickyNotes = new HashSet<>();

    public Tag() {
    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(Long id, String name, Set<Diary> diaries, Set<StickyNote> stickyNotes) {
        this.id = id;
        this.name = name;
        this.diaries = diaries;
        this.stickyNotes = stickyNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<Diary> getDiaries() {
        return diaries;
    }

    public void setDiaries(Set<Diary> diaries) {
        this.diaries = diaries;
    }

    public Set<StickyNote> getStickyNotes() {
        return stickyNotes;
    }

    public void setStickyNotes(Set<StickyNote> stickyNotes) {
        this.stickyNotes = stickyNotes;
    }
}

