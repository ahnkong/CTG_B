package com.ctg.backend.service;

import com.ctg.backend.dto.CommunityDTO;
import com.ctg.backend.entity.*;
import com.ctg.backend.repository.CommunityRepository;
import com.ctg.backend.repository.CommentRepository;
import com.ctg.backend.repository.ReCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {
    
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final ReCommentRepository reCommentRepository;
    private final ImageService imageService;
    private final UserService userService;

    @Transactional
    public Community createCommunity(CommunityDTO communityDTO, List<MultipartFile> images) throws IOException {
        User user = userService.findByEmail(userService.getEmailFromToken(null)).toUser();
        
        Community community = new Community();
        community.setTitle(communityDTO.getTitle());
        community.setContent(communityDTO.getContent());
        community.setCommunityType(communityDTO.getCommunityType());
        community.setContentStatus(ContentStatus.ACTIVE);
        community.setUser(user);
        community.setDomain(user.getDomain());
        
        Community savedCommunity = communityRepository.save(community);
        
        if (images != null && !images.isEmpty()) {
            imageService.saveImages(images, savedCommunity, BoardType.COMMUNITY);
        }
        
        return savedCommunity;
    }

    @Transactional
    public Community updateCommunity(Long boardId, CommunityDTO communityDTO, List<MultipartFile> newImages) throws IOException {
        Community community = communityRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        community.setTitle(communityDTO.getTitle());
        community.setContent(communityDTO.getContent());
        community.setCommunityType(communityDTO.getCommunityType());
        
        if (newImages != null && !newImages.isEmpty()) {
            imageService.deleteImages(community, BoardType.COMMUNITY);
            imageService.saveImages(newImages, community, BoardType.COMMUNITY);
        }
        
        return communityRepository.save(community);
    }

    @Transactional
    public void deleteCommunity(Long boardId) {
        Community community = communityRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        community.setContentStatus(ContentStatus.DELETED);
        
        // 연관된 댓글들의 상태도 DELETE로 변경
        List<Comment> comments = community.getComments();
        comments.forEach(comment -> {
            comment.setContentStatus(ContentStatus.DELETED);
            comment.getReComments().forEach(reComment -> 
                reComment.setContentStatus(ContentStatus.DELETED));
        });
        
        communityRepository.save(community);
    }

    @Transactional(readOnly = true)
    public Community getCommunity(Long boardId) {
        return communityRepository.findById(boardId)
                .filter(community -> community.getContentStatus() == ContentStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Community> searchCommunities(String keyword) {
        return communityRepository.searchByTitleOrContent(keyword, ContentStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Community> getMyPosts() {
        User user = userService.findByEmail(userService.getEmailFromToken(null)).toUser();
        return communityRepository.findByUserAndContentStatus(user, ContentStatus.ACTIVE);
    }
} 