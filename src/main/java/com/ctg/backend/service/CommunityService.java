package com.ctg.backend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ctg.backend.dto.CommunityDTO;
import com.ctg.backend.entity.Community;
import com.ctg.backend.entity.CommunityType;
import com.ctg.backend.entity.ContentStatus;
import com.ctg.backend.entity.BoardType;
import com.ctg.backend.repository.CommunityRepository;
import com.ctg.backend.repository.UserRepository;

@Service
public class CommunityService {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    // 커뮤니티 타입 유효성 검사
    public void validateCommunityType(String type) {
        try {
            CommunityType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid community type: " + type);
        }
    }

    // 게시글 생성
    @Transactional
    public CommunityDTO createCommunity(CommunityDTO communityDTO, List<MultipartFile> images) throws IOException {
        Community community = new Community();
        community.setTitle(communityDTO.getTitle());
        community.setContent(communityDTO.getContent());
        community.setCommunityType(communityDTO.getCommunityType());
        community.setContentStatus(ContentStatus.ACTIVE);
        community.setView(0);
        community.setUser(userRepository.findById(communityDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        Community savedCommunity = communityRepository.save(community);

        // 이미지 처리
        if (images != null && !images.isEmpty()) {
            imageService.saveImages(images, savedCommunity, BoardType.COMMUNITY);
        }

        return new CommunityDTO(savedCommunity);
    }

    // 게시글 수정
    @Transactional
    public CommunityDTO updateCommunity(Long boardId, CommunityDTO communityDTO, List<MultipartFile> images) throws IOException {
        Community community = communityRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));

        community.setTitle(communityDTO.getTitle());
        community.setContent(communityDTO.getContent());
        community.setCommunityType(communityDTO.getCommunityType());

        // 기존 이미지 삭제
        imageService.deleteImages(community, BoardType.COMMUNITY);

        // 새 이미지 저장
        if (images != null && !images.isEmpty()) {
            imageService.saveImages(images, community, BoardType.COMMUNITY);
        }

        Community updatedCommunity = communityRepository.save(community);
        return new CommunityDTO(updatedCommunity);
    }

    // 게시글 삭제
    @Transactional
    public void deleteCommunity(Long boardId) {
        Community community = communityRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));
        
        // 이미지 삭제
        imageService.deleteImages(community, BoardType.COMMUNITY);
        
        communityRepository.delete(community);
    }

    // 특정 게시글 조회
    @Transactional(readOnly = true)
    public CommunityDTO getCommunityById(Long boardId) {
        Community community = communityRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));
        return new CommunityDTO(community);
    }

    // 조회수 증가
    @Transactional
    public void incrementViews(Long boardId) {
        Community community = communityRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));
        community.setView(community.getView() + 1);
        communityRepository.save(community);
    }

    // 검색 및 정렬된 게시글 조회
    @Transactional(readOnly = true)
    public Page<CommunityDTO> getCommunities(String search, Integer filterDate, String sort, String type, Pageable pageable) {
        // TODO: 검색, 필터링, 정렬 로직 구현
        Page<Community> communities = communityRepository.findAll(pageable);
        return communities.map(CommunityDTO::new);
    }

    // 내가 작성한 게시글 조회
    @Transactional(readOnly = true)
    public Page<CommunityDTO> getMyCommunities(String userId, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Community> communities = communityRepository.findByUserUserId(userId, pageable);
        return communities.map(CommunityDTO::new);
    }
} 