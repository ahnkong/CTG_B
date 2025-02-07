package com.hoolhool.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoolhool.backend.entity.ReComment;

public interface ReCommentRepository extends JpaRepository<ReComment, Long> {
    
    // 특정 댓글에 속한 대댓글 조회
    List<ReComment> findByComment_CommentId(Long commentId);

    // 특정 사용자가 작성한 대댓글 조회
    List<ReComment> findByUserId(String userId);

    // 특정 키워드가 포함된 대댓글 검색
    List<ReComment> findByContentContaining(String keyword);

    // 특정 댓글의 대댓글 삭제
    void deleteByComment_CommentId(Long commentId);
}
