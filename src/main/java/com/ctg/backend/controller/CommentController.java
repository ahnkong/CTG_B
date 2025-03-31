package com.ctg.backend.controller;

import java.util.List;
import java.util.Map;

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

import com.ctg.backend.dto.CommentDTO;
import com.ctg.backend.service.CommentService;

@RestController
@RequestMapping("/api/v1/comments")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.createComment(commentDTO);
        return ResponseEntity.ok(createdComment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody Map<String, String> payload) { // JSON으로 받기
        String content = payload.get("content"); // "content" 키 값만 추출
        CommentDTO updatedComment = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    // 특정 게시글의 댓글 조회
    @GetMapping("/by-board/{boardId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBoardId(@PathVariable Long boardId, @RequestParam String userId) {
        List<CommentDTO> comments = commentService.getCommentsByBoardId(boardId, userId);
        return ResponseEntity.ok(comments); // 댓글과 대댓글 포함 리스트 반환
    }

    // 특정 사용자가 작성한 댓글 조회
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUserId(@PathVariable String userId) {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 검색
    @GetMapping("/search")
    public ResponseEntity<List<CommentDTO>> searchComments(@RequestParam String keyword) {
        List<CommentDTO> comments = commentService.searchComments(keyword);
        return ResponseEntity.ok(comments);
    }

    // 특정 게시글의 전체 댓글 수 반환 (댓글 수 + 대댓글 수)
    @GetMapping("/count/{boardId}")
    public ResponseEntity<Long> getTotalCommentCount(@PathVariable Long boardId) {
        long totalComments = commentService.countTotalComments(boardId);
        return ResponseEntity.ok(totalComments);
    }

}
