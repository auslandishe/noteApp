package com.example.noteappbackend.service;

import com.example.noteappbackend.dto.StickyNoteDTO;
import com.example.noteappbackend.entity.StickyNote;
import com.example.noteappbackend.entity.Tag;
import com.example.noteappbackend.entity.User;
import com.example.noteappbackend.repository.StickyNoteRepository;
import com.example.noteappbackend.repository.TagRepository;
import com.example.noteappbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StickyNoteService {

    private final StickyNoteRepository stickyNoteRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public StickyNoteService(StickyNoteRepository stickyNoteRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.stickyNoteRepository = stickyNoteRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    public List<StickyNoteDTO> getAllStickyNotesByUser(Long userId) {
        return stickyNoteRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StickyNoteDTO createStickyNote(StickyNoteDTO stickyNoteDTO) {
        StickyNote note = new StickyNote();
        note.setTitle(stickyNoteDTO.getTitle());
        note.setContent(stickyNoteDTO.getContent());
        note.setColor(stickyNoteDTO.getColor());
        note.setPosition_x(stickyNoteDTO.getPositionX());
        note.setPosition_y(stickyNoteDTO.getPositionY());

        User user = userRepository.findById(stickyNoteDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        note.setUser(user);

        // 若 tag 名稱存在則使用，否則建立新 tag
        List<String> tagNames = stickyNoteDTO.getTags();
        Set<Tag> tags = tagNames== null ? new HashSet<>(): stickyNoteDTO.getTags().stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .collect(Collectors.toSet());
        note.setTags(tags);

        StickyNote newStickyNote = stickyNoteRepository.save(note);
        return toDTO(newStickyNote);
    }

    @Transactional
    public StickyNoteDTO updateStickyNote(Long id, StickyNoteDTO stickyNoteDTO) {
        StickyNote note = stickyNoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StickyNote not found"));

        note.setTitle(stickyNoteDTO.getTitle());
        note.setContent(stickyNoteDTO.getContent());
        note.setColor(stickyNoteDTO.getColor());
        note.setPosition_x(stickyNoteDTO.getPositionX());
        note.setPosition_y(stickyNoteDTO.getPositionY());

        // 更新 tags
        List<String> tagNames = stickyNoteDTO.getTags();
        Set<Tag> tags = tagNames== null ? new HashSet<>(): stickyNoteDTO.getTags().stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .collect(Collectors.toSet());
        note.setTags(tags);

        return toDTO(stickyNoteRepository.save(note));
    }

    public void deleteStickyNote(Long id) {
        if (!stickyNoteRepository.existsById(id)) {
            throw new RuntimeException("Sticky note with ID " + id + " not found.");
        }
        stickyNoteRepository.deleteById(id);
    }


    private StickyNoteDTO toDTO(StickyNote note) {
        StickyNoteDTO dto = new StickyNoteDTO();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setColor(note.getColor());
        dto.setPositionX(note.getPosition_x());
        dto.setPositionY(note.getPosition_y());
        dto.setUserId(note.getUser().getId());
        dto.setTags(note.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        return dto;
    }
}

