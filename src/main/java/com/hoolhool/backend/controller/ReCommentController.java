package com.hoolhool.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoolhool.backend.dto.ReCommentDTO;
import com.hoolhool.backend.service.ReCommentService;

@RestController
@RequestMapping("/api/v1/recomments")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class ReCommentController {
    
    @Autowired
    private ReCommentService reCommentService;

    // 대댓글 생성
    @PostMapping
    public ResponseEntity<ReCommentDTO> createReComment(@RequestBody ReCommentDTO reCommentDTO) {
        ReCommentDTO createdReComment = reCommentService.createReComment(reCommentDTO);
        return ResponseEntity.ok(createdReComment);
    }

    // 대댓글 수정
    @PutMapping("/{reCommentId}")
    public ResponseEntity<ReCommentDTO> updateReComment(
            @PathVariable Long reCommentId,
            @RequestBody String content) {
        ReCommentDTO updatedReComment = reCommentService.updateReComment(reCommentId, content);
        return ResponseEntity.ok(updatedReComment);
    }

    // 대댓글 삭제
    @DeleteMapping("/{reCommentId}")
    public ResponseEntity<String> deleteReComment(@PathVariable Long reCommentId) {
        reCommentService.deleteReComment(reCommentId);
        return ResponseEntity.ok("대댓글이 삭제되었습니다.");
    }

    // 특정 댓글의 대댓글 조회
    @GetMapping("/by-comment/{commentId}")
    public ResponseEntity<List<ReCommentDTO>> getReCommentsByCommentId(@PathVariable Long commentId) {
        List<ReCommentDTO> reComments = reCommentService.getReCommentsByCommentId(commentId);
        return ResponseEntity.ok(reComments);
    }

}
