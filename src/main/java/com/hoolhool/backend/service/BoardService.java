package com.hoolhool.backend.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hoolhool.backend.dto.BoardDTO;
import com.hoolhool.backend.entity.Board;
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

    // 조회수 또는 좋아요 수 정렬된 게시글 반환
    public Page<BoardDTO> getBoardsSorted(String sort, Pageable pageable) {
        if ("views".equalsIgnoreCase(sort)) {
            // 조회수 기준 정렬
            return boardRepository.findAllByOrderByViewDesc(pageable)
                    .map(this::convertToDTO);
        } else if ("likes".equalsIgnoreCase(sort)) {
            // 좋아요 수 기준 정렬
            return boardRepository.findAllByLikesCount(pageable)
                    .map(this::convertToDTO);
        }
        // 기본 정렬 (작성일 내림차순 등)
        return boardRepository.findAll(pageable).map(this::convertToDTO);
    }

    // 게시글 목록 반환 (검색, 필터링, 정렬 포함)
    public Page<BoardDTO> getBoards(String search, Integer filterDate, String sort, Pageable pageable) {
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
        // DTO를 엔티티로 변환
        Board board = convertToEntity(boardDTO);
        board.setCDate(LocalDateTime.now());
        board.setView(0);
        board.setHidden(false);

        // 게시글 저장
        Board savedBoard = boardRepository.save(board);

        // 이미지 저장
        if (images != null && !images.isEmpty()) {
            imageService.saveImages(images, savedBoard.getBoardId());
        }

        return convertToDTO(savedBoard);
    }

    
    //게시글 수정
    public BoardDTO updateBoard(Long boardId, BoardDTO boardDTO, List<MultipartFile> newImages) {
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
                board.getCommentId()
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
                boardDTO.getCommentId()
        );
    }
}
