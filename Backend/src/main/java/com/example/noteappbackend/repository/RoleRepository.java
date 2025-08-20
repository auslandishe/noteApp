package com.example.noteappbackend.repository;

import com.example.noteappbackend.Enum.Roles;
import com.example.noteappbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(Roles role);
}
