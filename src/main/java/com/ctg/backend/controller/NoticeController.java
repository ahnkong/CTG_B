package com.ctg.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ctg.backend.dto.NoticeDTO;
import com.ctg.backend.entity.NoticeType;
import com.ctg.backend.service.NoticeService;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<NoticeDTO> createNotice(
            @RequestBody NoticeDTO noticeDTO,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {
        return ResponseEntity.ok(noticeService.createNotice(noticeDTO, images));
    }

    // 게시글 수정
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeDTO> updateNotice(
            @PathVariable Long noticeId,
            @RequestBody NoticeDTO noticeDTO,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {
        return ResponseEntity.ok(noticeService.updateNotice(noticeId, noticeDTO, images));
    }

    // 게시글 삭제
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }

    // 특정 게시글 조회
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDTO> getNoticeById(
            @PathVariable Long noticeId,
            @RequestParam(required = false) Long userId) {
        noticeService.incrementViews(noticeId);
        return ResponseEntity.ok(noticeService.getNoticeById(noticeId, userId));
    }

    // 게시글 목록 조회 (검색, 필터링, 정렬)
    @GetMapping
    public ResponseEntity<Page<NoticeDTO>> getNotices(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long domainId,
            @RequestParam(required = false) NoticeType noticeType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(noticeService.getNotices(search, sort, domainId, noticeType, pageable));
    }

    // 내가 작성한 게시글 조회
    @GetMapping("/my")
    public ResponseEntity<Page<NoticeDTO>> getMyNotices(
            @RequestParam Long userId,
            @RequestParam(required = false) NoticeType noticeType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(noticeService.getMyNotices(userId, noticeType, page, size));
    }
}