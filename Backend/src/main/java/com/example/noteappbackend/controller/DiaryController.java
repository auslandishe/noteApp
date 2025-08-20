package com.example.noteappbackend.controller;

import com.example.noteappbackend.dto.CreateDiaryRequest;
import com.example.noteappbackend.dto.DiaryDTO;
import com.example.noteappbackend.dto.UpdateDiaryRequest;
import com.example.noteappbackend.security.CustomUserDetails;
import com.example.noteappbackend.service.DiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // 建立日記
    @PostMapping
    public ResponseEntity<DiaryDTO> createDiary(
            @RequestBody CreateDiaryRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser // 自定義 UserDetails
    ) {
        DiaryDTO created = diaryService.createDiary(request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 查詢目前使用者的所有日記
    @GetMapping
    public ResponseEntity<List<DiaryDTO>> getMyDiaries(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        // 檢查 currentUser 是否為 null
        if (currentUser == null) {
            // 如果是 null，代表未登入，返回空列表或所有公開日記
            // 這取決於你的業務邏輯
            return ResponseEntity.ok(List.of());
        }
        List<DiaryDTO> diaries = diaryService.getDiariesByUserId(currentUser.getId());
        return ResponseEntity.ok(diaries);
    }

    // 查詢單一日記（確認擁有者）
    @GetMapping("/{id}")
    public ResponseEntity<DiaryDTO> getDiaryById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        DiaryDTO diary = diaryService.getDiaryById(id);
        if (!diary.getUserId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(diary);
    }

    // 刪除日記
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        diaryService.deleteDiary(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryDTO> updateDiary(
            @PathVariable Long id,
            @RequestBody UpdateDiaryRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        DiaryDTO updated = diaryService.updateDiary(id, currentUser.getId(), request);
        return ResponseEntity.ok(updated);
    }

}

