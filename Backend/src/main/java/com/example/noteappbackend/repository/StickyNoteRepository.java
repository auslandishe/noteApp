package com.example.noteappbackend.repository;
import com.example.noteappbackend.entity.StickyNote;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.noteappbackend.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StickyNoteRepository extends JpaRepository<StickyNote, Long> {
    List<StickyNote> findByUser(User user);
    List<StickyNote> findByUserId(Long userId);
}
