package com.hoolhool.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.hoolhool.backend.entity.Board;
import com.hoolhool.backend.entity.Comment;
import com.hoolhool.backend.entity.Image;

public interface BoardRepository extends JpaRepository<Board, Long> {
    
    // 제목 또는 내용에 검색어가 포함된 게시글 조회
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    // 특정 날짜 이후에 생성된 게시글 조회
    Page<Board> findByCDateAfter(LocalDateTime startDate, Pageable pageable);

    // 좋아요 수 기준 게시글 정렬
    @Query("SELECT b FROM Board b LEFT JOIN Like l ON l.boardId = b.boardId WHERE l.type = 'BOARD' GROUP BY b ORDER BY COUNT(l) DESC")
    Page<Board> findAllByLikesCount(Pageable pageable);

    // 조회수 기준 게시글 정렬
    Page<Board> findAllByOrderByViewDesc(Pageable pageable);

    // 댓글 조회
    @Query("SELECT c FROM Comment c WHERE c.boardId = :boardId AND c.reCommentId IS NULL ORDER BY c.coCDate DESC")
    Page<Comment> findCommentsByBoardId(Long boardId, Pageable pageable);

    // 이미지 조회
    @Query("SELECT i FROM Image i WHERE i.boardId = :boardId ORDER BY i.imageOrder")
    List<Image> findImagesByBoardId(Long boardId);

}
