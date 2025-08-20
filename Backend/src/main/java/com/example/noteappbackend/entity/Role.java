package com.example.noteappbackend.entity;

import com.example.noteappbackend.Enum.Roles;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private Roles roleName;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission",
            joinColumns = @JoinColumn(name="role_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="permission_id", referencedColumnName="id"))
    private Set<Permission> permissions = new HashSet<>();
    public Role() {
    }

    public Role(long id, Roles roleName, Set<Permission> permissions) {
        this.id = id;
        this.roleName = roleName;
        this.permissions = permissions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Roles getRoleName() {
        return roleName;
    }

    public void setRoleName(Roles roleName) {
        this.roleName = roleName;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
