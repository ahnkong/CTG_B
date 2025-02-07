package com.hoolhool.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoolhool.backend.dto.BoardDTO;
import com.hoolhool.backend.entity.Board;
import com.hoolhool.backend.entity.BoardType;
import com.hoolhool.backend.entity.Comment;
import com.hoolhool.backend.entity.Image;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 좋아요 수 기준 게시글 정렬
    @Query("SELECT b FROM Board b LEFT JOIN Like l ON l.board = b WHERE l.type = com.hoolhool.backend.entity.LikeType.BOARD GROUP BY b ORDER BY COUNT(l) DESC")
    Page<Board> findAllByLikesCount(Pageable pageable);

    // 검색 + 타입 + 날짜 필터 적용 (모든 조건을 포함한 동적 쿼리)
    @Query("SELECT b FROM Board b WHERE " +
       "(:search IS NULL OR b.title LIKE CONCAT('%', :search, '%') OR b.content LIKE CONCAT('%', :search, '%')) AND " +
       "(:type IS NULL OR b.type = :type) AND " +
       "(:startDate IS NULL OR b.cDate >= :startDate) " +
       "ORDER BY b.cDate DESC")
    Page<Board> findBoardsWithFilters(@Param("search") String search,
                                    @Param("type") BoardType type,  // 타입 변경
                                    @Param("startDate") LocalDateTime startDate,
                                    Pageable pageable); 

    // 조회수 기준 정렬 
    Page<Board> findByOrderByViewDesc(Pageable pageable);

    // 최신순 정렬
    Page<Board> findByOrderByCDateDesc(Pageable pageable);

    // 댓글 조회
    @Query("SELECT c FROM Comment c WHERE c.board.boardId = :boardId ORDER BY c.coCDate DESC")
    Page<Comment> findCommentsByBoardId(Long boardId, Pageable pageable);

    // 이미지 조회
    @Query("SELECT i FROM Image i WHERE i.board.boardId = :boardId ORDER BY i.imageOrder")
    List<Image> findImagesByBoardId(Long boardId);

    // 좋아요 개수 기준 + 타입별 정렬
    @Query("SELECT b FROM Board b " +
       "LEFT JOIN b.likes l " +
       "WHERE l.type = :type " +
       "GROUP BY b " +
       "ORDER BY COUNT(l) DESC, b.cDate DESC")
    Page<Board> findAllByLikesCountAndType(BoardType type, Pageable pageable);

    // 조회수 기준 + 타입별 정렬
    Page<Board> findByTypeOrderByViewDesc(BoardType type, Pageable pageable);

    // BoardType의 게시글에 속한 댓글만 조회
    @Query("SELECT c FROM Comment c JOIN Board b ON c.board.boardId = b.boardId WHERE b.type = :type AND c.reComments IS EMPTY ORDER BY c.coCDate DESC")
    Page<Comment> findCommentsByBoardType(BoardType type, Pageable pageable);

    /* 임시저장 */
    // 특정 사용자의 DRAFT 상태 게시글 찾기
    Optional<Board> findByUserIdAndStatus(String userId, String status);

    // 특정 사용자의 DRAFT 게시글 존재 여부 확인
    boolean existsByUserIdAndStatus(String userId, String status);

    // 오래된 DRAFT 데이터 조회
    List<Board> findAllByStatusAndLastSavedAtBefore(String status, LocalDateTime threshold);

    // 해시태그 검색
    @Query("SELECT b FROM Board b WHERE b.hashTag LIKE CONCAT('%', :tag, '%')")
    List<Board> findByHashTag(@Param("tag") String tag);

    // 제목 또는 내용에 검색어가 포함된 게시글 조회 (기존 `searchBoards`에서 사용)
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

   @Query(value = "SELECT b.*, COALESCE(l.like_count, 0) AS like_count " +
        "FROM board b " +
        "LEFT JOIN (" +
        "    SELECT board_id, COUNT(*) AS like_count " +
        "    FROM likes " +
        "    WHERE like_date >= :startDate " +
        "    GROUP BY board_id " +
        ") l ON b.board_id = l.board_id " +
        "WHERE (:type IS NULL OR b.type = :type) " +
        "ORDER BY like_count DESC",
        countQuery = "SELECT COUNT(*) FROM board b",
        nativeQuery = true)
   Page<Board> findByLikesWithinDateRange(@Param("type") BoardType type,
                                          @Param("startDate") LocalDateTime startDate,
                                          Pageable pageable);
}
