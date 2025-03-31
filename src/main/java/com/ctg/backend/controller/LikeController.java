package com.ctg.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctg.backend.entity.LikeType;
import com.ctg.backend.service.LikeService;

@RestController
@RequestMapping("/api/v1/likes")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class LikeController {
    
    @Autowired
    private LikeService likeService;

    // 좋아요 클릭, 취소
    @PatchMapping("/{type}/{id}")
    public ResponseEntity<String> toggleLike(
            @PathVariable LikeType type, // type을 LikeType Enum으로 변경
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        
        String userId = requestBody.get("userId");

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        String message = likeService.toggleLike(userId, type, id);
        return ResponseEntity.ok(message);
    }

    // 좋아요 상태 확인
    @GetMapping("/{type}/{id}/status")
    public ResponseEntity<Boolean> isLiked(
            @PathVariable LikeType type, // type을 LikeType Enum으로 변경
            @PathVariable Long id,
            @RequestParam String userId) {
        boolean isLiked = likeService.isLikedByUser(userId, type, id);
        return ResponseEntity.ok(isLiked);
    }

    // 좋아요 개수 조회
    @GetMapping("/{type}/{id}/count")
    public ResponseEntity<Long> getLikeCount(
            @PathVariable LikeType type, // type을 LikeType Enum으로 변경
            @PathVariable Long id) {
        long count = likeService.countLikes(type, id);
        return ResponseEntity.ok(count);
    }
}
