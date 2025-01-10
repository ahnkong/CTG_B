package com.hoolhool.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.hoolhool.backend.entity.Board;
import com.hoolhool.backend.entity.BoardType;
import com.hoolhool.backend.entity.Comment;
import com.hoolhool.backend.entity.Image;

public interface BoardRepository extends JpaRepository<Board, Long> {
    
    // 제목 또는 내용에 검색어가 포함된 게시글 조회
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    // 특정 날짜 이후에 생성된 게시글 조회
    Page<Board> findByCDateAfter(LocalDateTime startDate, Pageable pageable);

    // 좋아요 수 기준 게시글 정렬
    @Query("SELECT b FROM Board b LEFT JOIN Like l ON l.boardId = b.boardId WHERE l.type = com.hoolhool.backend.entity.LikeType.BOARD GROUP BY b ORDER BY COUNT(l) DESC")
    Page<Board> findAllByLikesCount(Pageable pageable);

    // 조회수 기준 게시글 정렬
    Page<Board> findAllByOrderByViewDesc(Pageable pageable);

    // 댓글 조회
    @Query("SELECT c FROM Comment c WHERE c.boardId = :boardId AND c.reCommentId IS NULL ORDER BY c.coCDate DESC")
    Page<Comment> findCommentsByBoardId(Long boardId, Pageable pageable);

    // 이미지 조회
    @Query("SELECT i FROM Image i WHERE i.boardId = :boardId ORDER BY i.imageOrder")
    List<Image> findImagesByBoardId(Long boardId);

    // 게시글 타입별 조회
    Page<Board> findByType(BoardType type, Pageable pageable);

    // 좋아요 개수 기준 + 타입별 정렬
    @Query("SELECT b FROM Board b LEFT JOIN Like l ON l.boardId = b.boardId WHERE l.type = com.hoolhool.backend.entity.LikeType.BOARD AND b.type = :type GROUP BY b ORDER BY COUNT(l) DESC")
    Page<Board> findAllByLikesCountAndType(BoardType type, Pageable pageable);

    // 조회수 기준 + 타입별 정렬
    Page<Board> findByTypeOrderByViewDesc(BoardType type, Pageable pageable);

    // BoardType의 게시글에 속한 댓글만 조회
    @Query("SELECT c FROM Comment c JOIN Board b ON c.boardId = b.boardId WHERE b.type = :type AND c.reCommentId IS NULL ORDER BY c.coCDate DESC")
    Page<Comment> findCommentsByBoardType(BoardType type, Pageable pageable);

    // 특정 사용자의 DRAFT 상태 게시글 찾기
    Optional<Board> findByUserIdAndStatus(String userId, String status);

    // 특정 사용자의 DRAFT 게시글 존재 여부 확인
    boolean existsByUserIdAndStatus(String userId, String status);

    // 오래된 DRAFT 데이터 조회
    List<Board> findAllByStatusAndLastSavedAtBefore(String status, LocalDateTime threshold);
}
