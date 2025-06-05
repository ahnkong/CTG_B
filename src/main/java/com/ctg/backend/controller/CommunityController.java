package com.ctg.backend.controller;

import com.ctg.backend.dto.CommunityDTO;
import com.ctg.backend.entity.Community;
import com.ctg.backend.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<?> createCommunity(
            @RequestPart("communityDTO") CommunityDTO communityDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Community community = communityService.createCommunity(communityDTO, images);
            return ResponseEntity.ok(community);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("이미지 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateCommunity(
            @PathVariable Long boardId,
            @RequestPart("communityDTO") CommunityDTO communityDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Community community = communityService.updateCommunity(boardId, communityDTO, images);
            return ResponseEntity.ok(community);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("이미지 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long boardId) {
        communityService.deleteCommunity(boardId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Community> getCommunity(@PathVariable Long boardId) {
        return ResponseEntity.ok(communityService.getCommunity(boardId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Community>> searchCommunities(@RequestParam String keyword) {
        return ResponseEntity.ok(communityService.searchCommunities(keyword));
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<Community>> getMyPosts() {
        return ResponseEntity.ok(communityService.getMyPosts());
    }
} 