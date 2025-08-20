package com.example.noteappbackend.controller;

import com.example.noteappbackend.dto.StickyNoteDTO;
import com.example.noteappbackend.service.StickyNoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sticky-note")
public class StickyNoteController {

    private final StickyNoteService stickyNoteService;

    public StickyNoteController(StickyNoteService stickyNoteService) {
        this.stickyNoteService = stickyNoteService;
    }

    // 🔹 建立便條紙
    @PostMapping
    public ResponseEntity<StickyNoteDTO> createStickyNote(@RequestBody StickyNoteDTO stickyNoteDTO) {
        StickyNoteDTO createdNote = stickyNoteService.createStickyNote(stickyNoteDTO);
        return ResponseEntity.ok(createdNote);
    }

    // 🔹 根據使用者 ID 查詢便條紙列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StickyNoteDTO>> getStickyNotesByUser(@PathVariable Long userId) {
        List<StickyNoteDTO> notes = stickyNoteService.getAllStickyNotesByUser(userId);
        return ResponseEntity.ok(notes);
    }

    // 🔹 更新便條紙
    @PutMapping("/{id}")
    public ResponseEntity<StickyNoteDTO> updateStickyNote(
            @PathVariable Long id,
            @RequestBody StickyNoteDTO stickyNoteDTO
    ) {
        StickyNoteDTO updatedNote = stickyNoteService.updateStickyNote(id, stickyNoteDTO);
        return ResponseEntity.ok(updatedNote);
    }

    // 🔹 刪除便條紙
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStickyNote(@PathVariable Long id) {
        stickyNoteService.deleteStickyNote(id);
        return ResponseEntity.noContent().build();
    }
}
