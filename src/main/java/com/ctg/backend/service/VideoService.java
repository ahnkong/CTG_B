package com.ctg.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctg.backend.dto.VideoDTO;
import com.ctg.backend.entity.Video;
import com.ctg.backend.entity.ContentStatus;
import com.ctg.backend.entity.LikeType;
import com.ctg.backend.entity.UserUpdate;
import com.ctg.backend.entity.UserRole;
import com.ctg.backend.repository.VideoRepository;
import com.ctg.backend.repository.UserRepository;
import com.ctg.backend.repository.DomainRepository;
import com.ctg.backend.repository.UserUpdateRepository;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private UserUpdateRepository userUpdateRepository;

    @Autowired
    private LikeService likeService;

    // 게시글 생성
    @Transactional
    public VideoDTO createVideo(VideoDTO videoDTO, Long userId) {
        UserUpdate userUpdate = userUpdateRepository.findByUser_UserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("UserUpdate not found"));
        UserRole role = userUpdate.getRole();
        if (!role.canCrudVideoBoard()) {
            throw new RuntimeException("비디오 게시판에 대한 권한이 없습니다.");
        }
        Video video = new Video();
        video.setTitle(videoDTO.getTitle());
        video.setVideoUrl(videoDTO.getVideoUrl());
        video.setPeacher(videoDTO.getPeacher());
        video.setVideoDate(videoDTO.getVideoDate());
        video.setReference(videoDTO.getReference());
        video.setSubTitle(videoDTO.getSubTitle());
        video.setContentStatus(ContentStatus.ACTIVE);
        video.setView(0);
        video.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        video.setDomain(domainRepository.findById(videoDTO.getDomainId())
                .orElseThrow(() -> new IllegalArgumentException("Domain not found")));

        Video savedVideo = videoRepository.save(video);
        return new VideoDTO(savedVideo);
    }

    // 게시글 수정
    @Transactional
    public VideoDTO updateVideo(Long videoId, VideoDTO videoDTO, Long userId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
        UserUpdate userUpdate = userUpdateRepository.findByUser_UserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("UserUpdate not found"));
        UserRole role = userUpdate.getRole();
        if (!role.canCrudVideoBoard()) {
            throw new RuntimeException("비디오 게시판에 대한 권한이 없습니다.");
        }
        if (!role.equals(UserRole.SUPER_ADMIN) && !video.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("본인 글만 수정할 수 있습니다.");
        }
        video.setTitle(videoDTO.getTitle());
        video.setVideoUrl(videoDTO.getVideoUrl());
        video.setPeacher(videoDTO.getPeacher());
        video.setVideoDate(videoDTO.getVideoDate());
        video.setReference(videoDTO.getReference());
        video.setSubTitle(videoDTO.getSubTitle());
        video.setUpdatedAt(LocalDateTime.now());

        Video updatedVideo = videoRepository.save(video);
        return new VideoDTO(updatedVideo);
    }

    // 게시글 삭제
    @Transactional
    public void deleteVideo(Long videoId, Long userId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
        UserUpdate userUpdate = userUpdateRepository.findByUser_UserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("UserUpdate not found"));
        UserRole role = userUpdate.getRole();
        if (!role.canCrudVideoBoard()) {
            throw new RuntimeException("비디오 게시판에 대한 권한이 없습니다.");
        }
        if (!role.equals(UserRole.SUPER_ADMIN) && !video.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("본인 글만 삭제할 수 있습니다.");
        }
        // ContentStatus를 DELETE로 변경
        video.setContentStatus(ContentStatus.DELETED);
        video.setUpdatedAt(LocalDateTime.now());
        videoRepository.save(video);
    }

    // 특정 게시글 조회
    @Transactional(readOnly = true)
    public VideoDTO getVideoById(Long videoId, Long userId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
        VideoDTO videoDTO = new VideoDTO(video);
        // 좋아요 여부 확인
        boolean isLiked = likeService.isLikedByUser(userId, LikeType.VIDEO, videoId);
        videoDTO.setLiked(isLiked);
        return videoDTO;
    }

    // 조회수 증가
    @Transactional
    public void incrementViews(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
        video.setView(video.getView() + 1);
        videoRepository.save(video);
    }

    // 검색 및 정렬된 게시글 조회
    @Transactional(readOnly = true)
    public Page<VideoDTO> getVideos(String search, String sort, Long domainId, Pageable pageable) {
        Page<Video> videos;
        if (search != null && !search.isEmpty()) {
            videos = videoRepository.searchByTitleOrSubTitle(search, ContentStatus.ACTIVE, pageable);
        } else if (domainId != null) {
            videos = videoRepository.findByDomain_DomainIdAndContentStatusOrderByVideoDateDesc(domainId, ContentStatus.ACTIVE, pageable);
        } else if ("view".equals(sort)) {
            videos = videoRepository.findAllOrderByViewDesc(ContentStatus.ACTIVE, pageable);
        } else if ("likes".equals(sort)) {
            videos = videoRepository.findAllOrderByLikesDesc(ContentStatus.ACTIVE, pageable);
        } else {
            // 기본적으로 videoDate 기준 내림차순 정렬
            videos = videoRepository.findByContentStatusOrderByVideoDateDesc(ContentStatus.ACTIVE, pageable);
        }
        return videos.map(VideoDTO::new);
    }

    // 내가 작성한 게시글 조회
    @Transactional(readOnly = true)
    public Page<VideoDTO> getMyVideos(Long userId, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Video> videos = videoRepository.findByUser_UserIdAndContentStatusOrderByVideoDateDesc(userId, ContentStatus.ACTIVE, pageable);
        return videos.map(VideoDTO::new);
    }
} 