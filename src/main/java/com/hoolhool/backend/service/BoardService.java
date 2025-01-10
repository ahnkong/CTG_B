package com.hoolhool.backend.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hoolhool.backend.dto.BoardDTO;
import com.hoolhool.backend.entity.Board;
import com.hoolhool.backend.entity.BoardType;
import com.hoolhool.backend.entity.Like;
import com.hoolhool.backend.repository.BoardRepository;
import com.hoolhool.backend.repository.CommentRepository;
import com.hoolhool.backend.repository.LikeRepository;
import com.hoolhool.backend.repository.ReCommentRepository;

@Service
public class BoardService {
    
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReCommentRepository reCommentRepository;

    public BoardService(BoardRepository boardRepository, ImageService imageService, LikeRepository likeRepository, CommentRepository commentRepository, ReCommentRepository reCommentRepository) {
        this.boardRepository = boardRepository;
        this.imageService = imageService;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.reCommentRepository = reCommentRepository;
    }
    
    // 태그 유효성 검사 메서드
    private void validateTags(String hashTag) {
        if (hashTag != null) {
            String[] tags = hashTag.split(",");
            if (tags.length > 5) {
                throw new IllegalArgumentException("태그는 최대 5개까지 입력 가능합니다.");
            }
        }
    }

    // BoardType 유효성 검사 메서드
    // 기존 private -> public으로 변경
    public void validateBoardType(String type) {
        try {
            BoardType.valueOf(type); // Enum 값 확인
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 BoardType입니다: " + type);
        }
    }

    // 타입별 게시글 필터링
    public Page<BoardDTO> getBoardsByType(BoardType type, Pageable pageable) {
        validateBoardType(type.toString());
        return boardRepository.findByType(type, pageable)
                .map(this::convertToDTO);
    }

    // 모든 게시글 반환 (DTO 변환 포함)
    public Page<BoardDTO> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable).map(this::convertToDTO);
    }

    // 특정 게시글 반환 (DTO 변환 포함)
    public BoardDTO getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
        return convertToDTO(board);
    }

    // 타입별 조회수 및 좋아요 수 정렬된 게시글 변환
    public Page<BoardDTO> getBoardsByTypeAndSort(BoardType type, String sort, Pageable pageable) {
        validateBoardType(type.toString());
    
        if ("views".equalsIgnoreCase(sort)) {
            return boardRepository.findByTypeOrderByViewDesc(type, pageable)
                    .map(this::convertToDTO);
        } else if ("likes".equalsIgnoreCase(sort)) {
            return boardRepository.findAllByLikesCountAndType(type, pageable)
                    .map(this::convertToDTO);
        }
    
        return boardRepository.findByType(type, pageable)
                .map(this::convertToDTO);
    }

    // 검색 및 정렬된 게시글 반환
    public Page<BoardDTO> getBoards(String search, BoardType type, Integer filterDate, String sort, Pageable pageable) {
        // 타입 필터링
        if (type != null) {
            return boardRepository.findByType(type, pageable).map(this::convertToDTO);
        }

        // 날짜 필터링
        if (filterDate != null) {
            LocalDateTime startDate = LocalDateTime.now().minusDays(filterDate);
            return boardRepository.findByCDateAfter(startDate, pageable).map(this::convertToDTO);
        }

        // 검색어 처리
        if (search != null && !search.isEmpty()) {
            return boardRepository.findByTitleContainingOrContentContaining(search, search, pageable)
                    .map(this::convertToDTO);
        }

        // 기본 정렬된 전체 게시글 반환
        return boardRepository.findAll(pageable).map(this::convertToDTO);
    }

    // 게시글 생성
    public BoardDTO createBoard(BoardDTO boardDTO, List<MultipartFile> images) throws IOException {
        validateBoardType(boardDTO.getType().toString()); // BoardType 유효성 검사
        validateTags(boardDTO.getHashTag()); // 태그 유효성 검사

        Board board = convertToEntity(boardDTO);
        board.setCDate(LocalDateTime.now());
        board.setView(0);
        board.setHidden(false);
        board.setStatus("PUBLISHED");

        Board savedBoard = boardRepository.save(board);

        if (images != null && !images.isEmpty()) {
            imageService.saveImages(images, savedBoard.getBoardId());
        }

        return convertToDTO(savedBoard);
    }

    
    //게시글 수정
    public BoardDTO updateBoard(Long boardId, BoardDTO boardDTO, List<MultipartFile> newImages) {
        validateBoardType(boardDTO.getType().toString()); // BoardType 유효성 검사 추가
        validateTags(boardDTO.getHashTag()); // 태그 유효성 검사

        // 기존 게시글 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));

        // 게시글 정보 업데이트
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setHashTag(boardDTO.getHashTag());
        board.setHidden(boardDTO.getHidden());
        board.setType(boardDTO.getType());

        // 기존 이미지를 삭제하고 새 이미지를 저장
        if (newImages != null && !newImages.isEmpty()) {
            try {
                // 기존 이미지 삭제
                imageService.deleteImagesByBoardId(boardId);
    
                // 새 이미지 저장
                imageService.saveImages(newImages, board.getBoardId());
                
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
            }
        }

        // 게시글 저장
        Board updatedBoard = boardRepository.save(board);

        return convertToDTO(updatedBoard);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        // 게시글 존재 여부 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
    
        // 게시글에 연결된 이미지 삭제
        imageService.deleteImagesByBoardId(boardId);

        // 게시글에 연결된 좋아요 삭제
        likeRepository.deleteByBoardId(boardId);
    
        // 게시글에 연결된 댓글 삭제
        commentRepository.findByBoardId(boardId).forEach(comment -> {
            // 댓글에 연결된 대댓글 삭제
            reCommentRepository.deleteByCommentId(comment.getCommentId());
            // 댓글 삭제
            commentRepository.deleteById(comment.getCommentId());
        });
    
        // 게시글 삭제
        boardRepository.delete(board);
    }

    // 검색어가 있을 때 검색어가 포함된 게시글 반환
    public Page<BoardDTO> searchBoards(String search, Pageable pageable) {
        return boardRepository.findByTitleContainingOrContentContaining(search, search, pageable)
                .map(this::convertToDTO);
    }

    // 조회수 증가
    public BoardDTO incrementViews(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));

        board.setView(board.getView() + 1);
        Board updatedBoard = boardRepository.save(board);

        return convertToDTO(updatedBoard);
    }

    /*
     * 임시저장 관련 기능
     */

    // 1. 임시 저장 생성/업데이트
    @Transactional
    public BoardDTO saveDraft(BoardDTO boardDTO, List<MultipartFile> images) throws IOException {
        Optional<Board> existingDraft = boardRepository.findByUserIdAndStatus(boardDTO.getUserId(), "DRAFT");

        Board board;
        if (existingDraft.isPresent()) {
            // 기존 임시 저장 데이터 업데이트
            board = existingDraft.get();
            board.setTitle(boardDTO.getTitle());
            board.setContent(boardDTO.getContent());
            board.setLastSavedAt(LocalDateTime.now());

            // 이미지 처리
            if (images != null && !images.isEmpty()) {
                imageService.deleteImagesByBoardId(board.getBoardId()); // 기존 이미지 삭제
                imageService.saveImages(images, board.getBoardId());    // 새 이미지 저장
            }
        } else {
            // 새로운 DRAFT 생성
            board = convertToEntity(boardDTO);
            board.setStatus("DRAFT");
            board.setLastSavedAt(LocalDateTime.now());
            Board savedBoard = boardRepository.save(board);

            // 이미지 저장
            if (images != null && !images.isEmpty()) {
                imageService.saveImages(images, savedBoard.getBoardId());
            }
        }
        return convertToDTO(board);
    }

    // 2. 임시 저장 불러오기
    public BoardDTO getDraft(String userId) {
        Board draft = boardRepository.findByUserIdAndStatus(userId, "DRAFT")
                .orElseThrow(() -> new RuntimeException("임시 저장된 게시글이 없습니다."));
        return convertToDTO(draft);
    }

    // 3. 게시로 전환
    @Transactional
    public BoardDTO publishDraft(Long boardId) {
        Board draft = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!"DRAFT".equals(draft.getStatus())) {
            throw new IllegalStateException("임시 저장된 게시글만 게시로 전환할 수 있습니다.");
        }

        draft.setStatus("PUBLISHED");
        draft.setLastSavedAt(LocalDateTime.now());

        Board publishedBoard = boardRepository.save(draft);
        return convertToDTO(publishedBoard);
    }

    // 4. 오래된 임시 저장 삭제 (정리 작업)
    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시 실행
    @Transactional
    public void cleanupOldDrafts() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);

        // 오래된 DRAFT 데이터 조회
        List<Board> oldDrafts = boardRepository.findAllByStatusAndLastSavedAtBefore("DRAFT", threshold);

        // 관련 이미지 삭제
        for (Board draft : oldDrafts) {
            imageService.deleteImagesByBoardId(draft.getBoardId());
        }

        // 오래된 DRAFT 데이터 삭제
        boardRepository.deleteAll(oldDrafts);
    }

    // 엔티티를 DTO로 변환
    private BoardDTO convertToDTO(Board board) {
        return new BoardDTO(
                board.getBoardId(),
                board.getUserId(),
                board.getContent(),
                board.getCDate(),
                board.getHashTag(),
                board.getHidden(),
                board.getTitle(),
                board.getView(),
                board.getType(),
                board.getCommentId(),
                board.getStatus(),
                board.getLastSavedAt()
        );
    }

    // DTO를 엔티티로 변환
    private Board convertToEntity(BoardDTO boardDTO) {
        return new Board(
                boardDTO.getBoardId(),
                boardDTO.getUserId(),
                boardDTO.getContent(),
                boardDTO.getcDate(),
                boardDTO.getHashTag(),
                boardDTO.getHidden(),
                boardDTO.getTitle(),
                boardDTO.getView(),
                boardDTO.getType(),
                boardDTO.getCommentId(),
                boardDTO.getStatus(),
                boardDTO.getLastSavedAt()
        );
    }
}
