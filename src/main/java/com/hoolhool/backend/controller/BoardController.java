package com.hoolhool.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hoolhool.backend.dto.BoardDTO;
import com.hoolhool.backend.service.BoardService;
import com.hoolhool.backend.service.ImageService;

@RestController
@RequestMapping("/api/v1/boards")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class BoardController {
    
    @Autowired
    private final BoardService boardService;

    @Autowired
    private final ImageService imageService;

    public BoardController(BoardService boardService, ImageService imageService) {
        this.boardService = boardService;
        this.imageService = imageService;
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<BoardDTO> createBoard(
            @RequestPart("board") BoardDTO boardDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            BoardDTO createdBoard = boardService.createBoard(boardDTO, images);
            return ResponseEntity.ok(createdBoard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDTO> updateBoard(
            @PathVariable Long boardId,
            @RequestPart("board") BoardDTO boardDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            BoardDTO updatedBoard = boardService.updateBoard(boardId, boardDTO, images);
            return ResponseEntity.ok(updatedBoard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok("게시글 및 연결된 이미지가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 특정 게시글 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long boardId) {
        try {
            BoardDTO board = boardService.getBoardById(boardId);
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // 검색 및 정렬된 게시글 조회
    @GetMapping
    public ResponseEntity<Page<BoardDTO>> getBoards(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "filterDate", required = false) Integer filterDate,
            @RequestParam(value = "sort", required = false) String sort,
            Pageable pageable) {
        try {
            Page<BoardDTO> boards = boardService.getBoards(search, filterDate, sort, pageable);
            return ResponseEntity.ok(boards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // 게시글 조회수 증가
    @PutMapping("/{boardId}/views")
    public ResponseEntity<BoardDTO> incrementViews(@PathVariable Long boardId) {
        try {
            BoardDTO updatedBoard = boardService.incrementViews(boardId);
            return ResponseEntity.ok(updatedBoard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // 정렬된 게시글 목록 조회
    @GetMapping("/sorted")
    public ResponseEntity<Page<BoardDTO>> getBoardsSorted(
            @RequestParam(value = "sort", required = false) String sort,
            Pageable pageable) {
        try {
            Page<BoardDTO> boards = boardService.getBoardsSorted(sort, pageable);
            return ResponseEntity.ok(boards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
}
