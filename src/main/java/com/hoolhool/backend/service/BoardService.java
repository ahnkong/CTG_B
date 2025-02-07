package com.hoolhool.backend.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    // BoardType 유효성 검사 메서드
    // 기존 private -> public으로 변경
    public void validateBoardType(String type) {
        try {
            BoardType.valueOf(type); // Enum 값 확인
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 BoardType입니다: " + type);
        }
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

    // 검색 및 정렬된 게시글 반환
    // 검색 + 타입 + 날짜 필터 + 정렬을 한 번에 처리
    public Page<BoardDTO> getBoards(String search, BoardType type, Integer filterDate, String sort, Pageable pageable) {

        // 입력 파라미터 로그
        System.out.println("==== getBoards 메서드 호출 ====");
        System.out.println("search: " + search);
        System.out.println("type: " + type);
        System.out.println("filterDate: " + filterDate);
        System.out.println("sort: " + sort);
        System.out.println("pageable: " + pageable);

        LocalDateTime startDate = (filterDate != null) ? LocalDateTime.now().minusDays(filterDate) : null;

        try {
            Page<Board> boardPage;

            // filterDate가 있다면, 해당 기간 동안 좋아요가 가장 많은 게시글 조회
            if (filterDate != null) {
                return boardRepository.findByLikesWithinDateRange(type, startDate, pageable)
                        .map(this::convertToDTO);
            }

            // 정렬 처리
            if (sort != null) {
                switch (sort.toLowerCase()) {
                    case "views":
                        return boardRepository.findByOrderByViewDesc(pageable).map(this::convertToDTO);
                    case "likes":
                        return boardRepository.findAllByLikesCount(pageable).map(this::convertToDTO);
                    case "latest":
                        return boardRepository.findByOrderByCDateDesc(pageable).map(this::convertToDTO);
                }
            }

            boardPage = boardRepository.findBoardsWithFilters(search, type, startDate, pageable);
    
            return boardPage.map(this::convertToDTO);
    
        } catch (Exception e) {
            // 예외 로그 출력
            System.err.println("getBoards 메서드 예외 발생: " + e.getMessage());
            e.printStackTrace();
            throw e; // 예외를 다시 던져 클라이언트에 전달
        }
    }

    // 게시글 생성
    public BoardDTO createBoard(BoardDTO boardDTO, List<MultipartFile> images) throws IOException {
        validateBoardType(boardDTO.getType().toString()); // BoardType 유효성 검사
        
        // 해시태그 처리
        String processedHashTags = processHashTags(boardDTO.getHashTag());
        boardDTO.setHashTag(processedHashTags);

        Board board = convertToEntity(boardDTO);
        
        board.setBoardId(null);
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
        
        // 해시태그 처리
        String processedHashTags = processHashTags(boardDTO.getHashTag());
        boardDTO.setHashTag(processedHashTags);

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
        likeRepository.deleteByBoard_BoardId(boardId);
    
        // 게시글에 연결된 댓글 삭제
        commentRepository.findByBoard_BoardId(boardId).forEach(comment -> {
            // 댓글에 연결된 대댓글 삭제
            reCommentRepository.deleteByComment_CommentId(comment.getCommentId());
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

    /* 해시태그 */
    // 태그 유효성 검사 및 처리 메서드
    private String processHashTags(String hashTagInput) {
        if (hashTagInput == null || hashTagInput.isEmpty()) {
            return null;
        }

        // 공백으로 구분된 해시태그를 배열로 변환
        String[] tags = hashTagInput.split("\\s+");

        // 태그 개수 제한
        if (tags.length > 5) {
            throw new IllegalArgumentException("해시태그는 최대 5개까지 입력 가능합니다.");
        }

        // 각 태그에서 # 제거 및 길이 검증
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].length() > 30) {
                throw new IllegalArgumentException("각 해시태그는 최대 30자까지 입력 가능합니다.");
            }
            tags[i] = tags[i].replaceFirst("#", ""); // # 기호 제거
        }

        // 콤마로 연결된 문자열로 반환
        return String.join(",", tags);
    }

    // 해시태그로 게시글 검색
    public List<BoardDTO> searchByHashTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            throw new IllegalArgumentException("해시태그는 비어 있을 수 없습니다.");
        }

        // # 제거 (클라이언트에서 입력받은 값 처리)
        String processedTag = tag.replaceFirst("#", "");

        // 레포지토리에서 검색
        List<Board> boards = boardRepository.findByHashTag(processedTag);

        // 결과를 DTO로 변환
        return boards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // 엔티티를 DTO로 변환
    private BoardDTO convertToDTO(Board board) {
        String hashTags = board.getHashTag();
        String processedHashTags = null;
    
        if (hashTags != null && !hashTags.isEmpty()) {
            processedHashTags = String.join(" ",
                    Stream.of(hashTags.split(","))
                            .map(tag -> "#" + tag)
                            .collect(Collectors.toList())
            );
        }
    
        return new BoardDTO(
                board.getBoardId(),
                board.getUserId(),
                board.getContent(),
                board.getCDate(),
                processedHashTags, // null 또는 해시태그 문자열 반환
                board.getHidden(),
                board.getTitle(),
                board.getView(),
                board.getType(),
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
                boardDTO.getStatus(),
                boardDTO.getLastSavedAt()
        );
    }
}
