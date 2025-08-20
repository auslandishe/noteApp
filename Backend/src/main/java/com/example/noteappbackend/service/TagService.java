package com.example.noteappbackend.service;

import com.example.noteappbackend.dto.TagDTO;
import com.example.noteappbackend.entity.Tag;
import com.example.noteappbackend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagDTO createTag(TagDTO tagDTO) {
        // 檢查名稱是否重複
        Optional<Tag> existingTag = tagRepository.findByName(tagDTO.getName());
        if (existingTag.isPresent()) {
            throw new RuntimeException("Tag名字已存在");
        }

        // 建立 Tag entity 並存入資料庫
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        tag.setName(tagDTO.getColor());

        Tag newTag = tagRepository.save(tag);

        return mapToDTO(newTag);
    }

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private TagDTO mapToDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setColor(tag.getColor());
        return dto;
    }
}


