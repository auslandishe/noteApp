package com.example.noteappbackend.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName="id"))
    private Set<Role> role = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StickyNote> stickyNotes = new ArrayList<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(long id, String username, String password, Set<Role> role, List<Diary> diaries, List<StickyNote> stickyNotes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.diaries = diaries;
        this.stickyNotes = stickyNotes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public List<Diary> getDiaries() {
        return diaries;
    }

    public void setDiaries(List<Diary> diaries) {
        this.diaries = diaries;
    }

    public List<StickyNote> getStickyNotes() {
        return stickyNotes;
    }

    public void setStickyNotes(List<StickyNote> stickyNotes) {
        this.stickyNotes = stickyNotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(role, user.role) && Objects.equals(diaries, user.diaries) && Objects.equals(stickyNotes, user.stickyNotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, diaries, stickyNotes);
    }
}
