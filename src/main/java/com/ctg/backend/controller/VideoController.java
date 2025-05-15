package com.ctg.backend.controller;

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

import com.ctg.backend.dto.VideoDTO;
import com.ctg.backend.service.VideoService;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<VideoDTO> createVideo(@RequestBody VideoDTO videoDTO, @RequestParam Long userId) {
        return ResponseEntity.ok(videoService.createVideo(videoDTO, userId));
    }

    // 게시글 수정
    @PutMapping("/{videoId}")
    public ResponseEntity<VideoDTO> updateVideo(
            @PathVariable Long videoId,
            @RequestBody VideoDTO videoDTO,
            @RequestParam Long userId) {
        return ResponseEntity.ok(videoService.updateVideo(videoId, videoDTO, userId));
    }

    // 게시글 삭제
    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId, @RequestParam Long userId) {
        videoService.deleteVideo(videoId, userId);
        return ResponseEntity.ok().build();
    }

    // 특정 게시글 조회
    @GetMapping("/{videoId}")
    public ResponseEntity<VideoDTO> getVideoById(
            @PathVariable Long videoId,
            @RequestParam(required = false) Long userId) {
        videoService.incrementViews(videoId);
        return ResponseEntity.ok(videoService.getVideoById(videoId, userId));
    }

    // 게시글 목록 조회 (검색, 필터링, 정렬)
    @GetMapping
    public ResponseEntity<Page<VideoDTO>> getVideos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long domainId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "videoDate"));
        return ResponseEntity.ok(videoService.getVideos(search, sort, domainId, pageable));
    }

    // 내가 작성한 게시글 조회
    @GetMapping("/my")
    public ResponseEntity<Page<VideoDTO>> getMyVideos(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.getMyVideos(userId, page, size));
    }
} 