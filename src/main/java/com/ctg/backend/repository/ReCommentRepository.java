package com.ctg.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctg.backend.entity.ReComment;

@Repository
public interface ReCommentRepository extends JpaRepository<ReComment, Long> {
    
    // 특정 댓글에 속한 대댓글 조회
    List<ReComment> findByComment_CommentId(Long commentId);

    // 특정 사용자가 작성한 대댓글 조회
    List<ReComment> findByUser_UserId(Long userId);

    // 특정 키워드가 포함된 대댓글 검색
    List<ReComment> findByContentContaining(String keyword);

    // 특정 댓글에 속한 활성화된 대댓글 조회
    List<ReComment> findByComment_CommentIdAndContentStatus(Long commentId, com.ctg.backend.entity.ContentStatus contentStatus);

    // 특정 키워드가 포함된 활성화된 대댓글 검색
    List<ReComment> findByContentContainingAndContentStatus(String keyword, com.ctg.backend.entity.ContentStatus contentStatus);
}
