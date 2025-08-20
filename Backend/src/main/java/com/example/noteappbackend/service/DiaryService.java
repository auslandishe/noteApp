package com.example.noteappbackend.service;

import com.example.noteappbackend.dto.CreateDiaryRequest;
import com.example.noteappbackend.dto.DiaryDTO;
import com.example.noteappbackend.dto.UpdateDiaryRequest;
import com.example.noteappbackend.entity.Diary;
import com.example.noteappbackend.entity.Tag;
import com.example.noteappbackend.entity.User;
import com.example.noteappbackend.repository.DiaryRepository;
import com.example.noteappbackend.repository.TagRepository;
import com.example.noteappbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    // 建立日記
    public DiaryDTO createDiary(CreateDiaryRequest createDiaryRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 處理標籤
        List<String> tagNames = createDiaryRequest.getTagNames();
        Set<Tag> tags = (tagNames == null ? new HashSet<>() :
                tagNames.stream().map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .collect(Collectors.toSet()));


        // 建立日記
        Diary diary = new Diary();
        diary.setTitle(createDiaryRequest.getTitle());
        diary.setAuthor(createDiaryRequest.getAuthor());
        diary.setContent(createDiaryRequest.getContent());
        diary.setUser(user);
        diary.setTags(tags);
        //保存新建立的日記
        diaryRepository.save(diary);
        //DiaryDTO給Controller傳回後端
        return toDto(diary);
    }

    // 查詢使用者所有日記
    public List<DiaryDTO> getDiariesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return diaryRepository.findByUser(user).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 查詢單篇日記
    public DiaryDTO getDiaryById(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Diary not found"));
    }

    // 刪除日記
    public void deleteDiary(Long diaryId, Long userId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("Diary not found"));

        Long diaryUserId = diary.getUser().getId();
        if (!diaryUserId.equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        diaryRepository.delete(diary);
    }

    @Transactional
    public DiaryDTO updateDiary(Long diaryId, Long userId, UpdateDiaryRequest request) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("Diary not found"));

        Long diaryUserId = diary.getUser().getId();
        // 權限檢查
        if (!diaryUserId.equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        diary.setAuthor((request.getAuthor()));
        diary.setTitle(request.getTitle());
        diary.setContent(request.getContent());

        // 處理標籤（舊的全部換掉）
        List<String> newTags = request.getTagNames();
        Set<Tag> tags = (newTags == null ? new HashSet<>() :
                newTags.stream().map(name -> tagRepository.findByName(name)
                                .orElseGet(() -> tagRepository.save(new Tag(name))))
                        .collect(Collectors.toSet()));


        diary.setTags(tags);

        return toDto(diary);
    }


    // 將 Diary 轉換為 DTO
    private DiaryDTO toDto(Diary diary) {
        DiaryDTO dto = new DiaryDTO();
        dto.setId(diary.getId());
        dto.setTitle(diary.getTitle());
        dto.setAuthor(diary.getAuthor());
        dto.setContent(diary.getContent());
        dto.setUserId(diary.getUser().getId());
        dto.setTags(
                diary.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList())
        );
        dto.setCreatedAt(diary.getCreatedAt().toString());
        dto.setUpdatedAt(diary.getUpdatedAt().toString());
        return dto;
    }
}
