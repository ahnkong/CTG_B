package com.ctg.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ctg.backend.dto.BoardDTO;
import com.ctg.backend.entity.Board;
import com.ctg.backend.entity.BoardType;
import com.ctg.backend.entity.Comment;
import com.ctg.backend.entity.Image;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // ✅ 조회수 증가 JPA Query
    @Modifying
    @Transactional
    @Query("UPDATE Board b SET b.view = b.view + 1 WHERE b.boardId = :boardId")
    void incrementViewCount(@Param("boardId") Long boardId);
    

    /* 정렬 */
    // 조회수 기준 + 타입별 정렬
    Page<Board> findByTypeOrderByViewDesc(BoardType type, Pageable pageable);

    // BoardType의 게시글에 속한 댓글만 조회
    @Query("SELECT c FROM Comment c JOIN Board b ON c.board.boardId = b.boardId WHERE b.type = :type AND c.reComments IS EMPTY ORDER BY c.coCDate DESC")
    Page<Comment> findCommentsByBoardType(BoardType type, Pageable pageable);

    // 좋아요 수 기준 게시글 정렬
    @Query("SELECT b FROM Board b LEFT JOIN Like l ON l.board = b WHERE l.type = com.ctg.backend.entity.LikeType.BOARD GROUP BY b ORDER BY COUNT(l) DESC")
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

    // ✅ [오늘의 게시글] 최신순 정렬용 메서드
    // type: 게시글 타입 (POSITIVE 또는 NEGATIVE)
    // cDate: 생성일 기준 내림차순 정렬
    Page<Board> findAllByType(BoardType type, Pageable pageable);



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
   
    // 검색어와 타입으로 게시글 조회 (최신순 정렬)
    Page<Board> findByTitleContainingAndType(String title, BoardType type, Pageable pageable);

    // 특정 기간 내 좋아요 수 기준 정렬
    // - filterDate: 최근 7일 또는 30일 (null이면 전체)
    // - type: 게시글 타입 (POSITIVE, NEGATIVE)
    @Query("SELECT b FROM Board b " +
           "LEFT JOIN b.likes l ON l.type = 'BOARD' " +
           "WHERE b.type = :type " +
           "AND (:filterDate IS NULL OR l.likeDate >= CURRENT_DATE - :filterDate) " +
           "GROUP BY b.boardId " +
           "ORDER BY COUNT(l.likeId) DESC")
    Page<Board> findBoardsOrderByLikeCount(
            @Param("type") BoardType type,
            @Param("filterDate") Integer filterDate,
            Pageable pageable
    );


    // ✅ 특정 유저가 작성한 게시글 찾기
    Page<Board> findByUserId(String userId, Pageable pageable);



}
