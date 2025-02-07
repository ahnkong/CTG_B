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
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoolhool.backend.dto.BoardDTO;
import com.hoolhool.backend.entity.BoardType;
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
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<BoardDTO> createBoard(
            @RequestPart("board") String boardJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            System.out.println("Received Raw JSON: " + boardJson);

            // JSON -> DTO 변환
            ObjectMapper objectMapper = new ObjectMapper();
            BoardDTO boardDTO = objectMapper.readValue(boardJson, BoardDTO.class);

            System.out.println("Converted BoardDTO: " + boardDTO);

            boardService.validateBoardType(boardDTO.getType().toString());
            BoardDTO createdBoard = boardService.createBoard(boardDTO, images);

            return ResponseEntity.ok(createdBoard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDTO> updateBoard(
            @PathVariable Long boardId,
            @RequestPart("board") BoardDTO boardDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            boardService.validateBoardType(boardDTO.getType().toString()); // BoardType 유효성 검사 추가
            BoardDTO updatedBoard = boardService.updateBoard(boardId, boardDTO, images);
            return ResponseEntity.ok(updatedBoard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok("게시글 및 연결된 이미지가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 특정 게시글 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long boardId) {
        try {
            BoardDTO board = boardService.getBoardById(boardId);
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 해시태그 검색
    @GetMapping("/search")
    public ResponseEntity<List<BoardDTO>> searchByHashTag(@RequestParam String tag) {
        try {
            List<BoardDTO> boards = boardService.searchByHashTag(tag);
            return ResponseEntity.ok(boards);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 검색 및 정렬된 게시글 조회
    @GetMapping
    public ResponseEntity<Page<BoardDTO>> getBoards(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "type", required = false) String type,  // 👈 String으로 입력 받음
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "filterDate", required = false) Integer filterDate, // 👈 날짜 필터 추가
            Pageable pageable) {
        try {
            BoardType boardType = null;
            if (type != null) {
                try {
                    boardType = BoardType.valueOf(type.toUpperCase()); // 👈 String → Enum 변환
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }

            Page<BoardDTO> boards = boardService.getBoards(search, boardType, filterDate, sort, pageable);
            return ResponseEntity.ok(boards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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

    /* 임시저장 */
    // 임시 저장 API
    @PostMapping(value = "/draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDTO> saveDraft(
            @RequestPart("board") BoardDTO boardDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            BoardDTO draft = boardService.saveDraft(boardDTO, images); // 이미지도 처리 가능하도록 수정
            return ResponseEntity.ok(draft);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게시글 게시 API
    @PutMapping("/{boardId}/publish")
    public ResponseEntity<BoardDTO> publishDraft(@PathVariable Long boardId) {
        try {
            BoardDTO publishedBoard = boardService.publishDraft(boardId);
            return ResponseEntity.ok(publishedBoard);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 임시 저장 데이터 불러오기 API
    @GetMapping("/draft")
    public ResponseEntity<BoardDTO> getDraft(@RequestParam String userId) {
        try {
            BoardDTO draft = boardService.getDraft(userId);
            return ResponseEntity.ok(draft);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 오래된 DRAFT 데이터 정리 API
    @DeleteMapping("/draft/cleanup")
    public ResponseEntity<String> cleanupDrafts() {
        try {
            boardService.cleanupOldDrafts();
            return ResponseEntity.ok("오래된 임시 저장 데이터를 정리했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("임시 저장 정리 중 오류가 발생했습니다.");
        }
    }
    
}
