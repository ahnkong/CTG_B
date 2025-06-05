package com.ctg.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import com.ctg.backend.dto.MyCommentDTO;
import com.ctg.backend.entity.BoardType;
import com.ctg.backend.service.CommentService;

@RestController
@RequestMapping("/api/v1/comments")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @RequestBody CommentDTO commentDTO,
            @RequestParam Long userId) {
        try {
            CommentDTO createdComment = commentService.createComment(commentDTO, userId);
        return ResponseEntity.ok(createdComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok(updatedComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 댓글 삭제 (ContentStatus를 DELETED로 변경)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @RequestParam Long userId) {
        try {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    // 특정 게시글의 댓글 조회 (게시글 타입 포함)
    @GetMapping("/board/{boardType}/{boardId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBoard(
            @PathVariable BoardType boardType,
            @PathVariable Long boardId,
            @RequestParam Long userId) {
        try {
            List<CommentDTO> comments = commentService.getCommentsByBoard(boardType, boardId, userId);
            return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 특정 사용자가 작성한 댓글 조회
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUserId(@PathVariable Long userId) {
        try {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 댓글 검색
    @GetMapping("/search")
    public ResponseEntity<List<CommentDTO>> searchComments(@RequestParam String keyword) {
        try {
        List<CommentDTO> comments = commentService.searchComments(keyword);
        return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 특정 게시글의 전체 댓글 수 반환 (댓글 수 + 대댓글 수)
    @GetMapping("/count/{boardType}/{boardId}")
    public ResponseEntity<Long> getTotalCommentCount(
            @PathVariable BoardType boardType,
            @PathVariable Long boardId) {
        try {
            long totalComments = commentService.countTotalComments(boardType, boardId);
        return ResponseEntity.ok(totalComments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
    //2025.04.28 댓글 리스트 불러오기
    // @GetMapping("/my-comments")
    // public ResponseEntity<Page<CommentDTO>> getMyComments(
    //     @RequestParam String userId,
    //     Pageable pageable) {
    //     try {
    //         Page<Comment> comments = commentRepository.findByUserIdOrderByCoCDateDesc(userId, pageable);
    //         Page<CommentDTO> commentDTOs = comments.map(commentService::convertToDTO);
    //         return ResponseEntity.ok(commentDTOs);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //     }
    // }

    @GetMapping("/myComments")
    public ResponseEntity<List<MyCommentDTO>> getMyComments(
        @RequestParam Long userId
    ) {
        try {
        List<MyCommentDTO> comments = commentService.getMyComments(userId);
        return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
