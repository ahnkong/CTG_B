package com.ctg.backend.controller;

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

import com.ctg.backend.dto.CommunityDTO;
import com.ctg.backend.entity.CommunityType;
import com.ctg.backend.service.CommunityService;
import com.ctg.backend.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/community")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class CommunityController {
    
    @Autowired
    private final CommunityService communityService;

    @Autowired
    private final ImageService imageService;

    public CommunityController(CommunityService communityService, ImageService imageService) {
        this.communityService = communityService;
        this.imageService = imageService;
    }

    // 게시글 생성
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CommunityDTO> createCommunity(
            @RequestPart("community") String communityJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CommunityDTO communityDTO = objectMapper.readValue(communityJson, CommunityDTO.class);

            communityService.validateCommunityType(communityDTO.getCommunityType().toString());
            CommunityDTO createdCommunity = communityService.createCommunity(communityDTO, images);

            return ResponseEntity.ok(createdCommunity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<CommunityDTO> updateCommunity(
            @PathVariable Long boardId,
            @RequestPart("community") CommunityDTO communityDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            communityService.validateCommunityType(communityDTO.getCommunityType().toString());
            CommunityDTO updatedCommunity = communityService.updateCommunity(boardId, communityDTO, images);
            return ResponseEntity.ok(updatedCommunity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteCommunity(@PathVariable Long boardId) {
        try {
            communityService.deleteCommunity(boardId);
            return ResponseEntity.ok("게시글 및 연결된 이미지가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 특정 게시글 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<CommunityDTO> getCommunityById(@PathVariable Long boardId) {
        try {
            // 조회수 증가
            communityService.incrementViews(boardId);
            
            CommunityDTO community = communityService.getCommunityById(boardId);
            return ResponseEntity.ok(community);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 검색 및 정렬된 게시글 조회
    @GetMapping
    public ResponseEntity<Page<CommunityDTO>> getCommunities(
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "filterDate", required = false) Integer filterDate,
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "type", required = false) String type,
        Pageable pageable) {

        try {
            Page<CommunityDTO> communities = communityService.getCommunities(search, filterDate, sort, type, pageable);
            return ResponseEntity.ok(communities);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 내가 작성한 게시글 조회
    @GetMapping("/myPosts")
    public ResponseEntity<Page<CommunityDTO>> getMyCommunities(
        @RequestParam String userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<CommunityDTO> communities = communityService.getMyCommunities(userId, page, size);
            return ResponseEntity.ok(communities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
} 