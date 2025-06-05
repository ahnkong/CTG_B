package com.ctg.backend.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ctg.backend.dto.NoticeDTO;
import com.ctg.backend.entity.Notice;
import com.ctg.backend.entity.ContentStatus;
import com.ctg.backend.entity.BoardType;
import com.ctg.backend.entity.NoticeType;
import com.ctg.backend.entity.LikeType;
import com.ctg.backend.repository.NoticeRepository;
import com.ctg.backend.repository.UserRepository;
import com.ctg.backend.repository.DomainRepository;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private LikeService likeService;

    // 게시글 생성
    @Transactional
    public NoticeDTO createNotice(NoticeDTO noticeDTO, List<MultipartFile> images) throws IOException {
        Notice notice = new Notice();
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setContentStatus(ContentStatus.ACTIVE);
        notice.setView(0);
        notice.setNoticeType(noticeDTO.getNoticeType());
        notice.setDisplayStartDate(noticeDTO.getDisplayStartDate());
        notice.setDisplayEndDate(noticeDTO.getDisplayEndDate());
        notice.setUser(userRepository.findById(noticeDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        notice.setDomain(domainRepository.findById(noticeDTO.getDomainId())
                .orElseThrow(() -> new IllegalArgumentException("Domain not found")));

        // 공지 상태 업데이트
        notice.updateStatus();

        Notice savedNotice = noticeRepository.save(notice);

        // 이미지 처리
        if (images != null && !images.isEmpty()) {
            imageService.saveImages(images, savedNotice, BoardType.NOTICE);
        }

        return new NoticeDTO(savedNotice);
    }

    // 게시글 수정
    @Transactional
    public NoticeDTO updateNotice(Long noticeId, NoticeDTO noticeDTO, List<MultipartFile> images) throws IOException {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));

        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setNoticeType(noticeDTO.getNoticeType());
        notice.setUpdatedAt(LocalDateTime.now());  // 수정 시간 업데이트

        // 기존 이미지 삭제
        imageService.deleteImages(notice, BoardType.NOTICE);

        // 새 이미지 저장
        if (images != null && !images.isEmpty()) {
            imageService.saveImages(images, notice, BoardType.NOTICE);
        }

        Notice updatedNotice = noticeRepository.save(notice);
        return new NoticeDTO(updatedNotice);
    }

    // 게시글 삭제
    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));
        
        // 이미지 삭제
        imageService.deleteImages(notice, BoardType.NOTICE);
        
        // ContentStatus를 DELETE로 변경
        notice.setContentStatus(ContentStatus.DELETED);
        notice.setUpdatedAt(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    // 특정 게시글 조회
    @Transactional(readOnly = true)
    public NoticeDTO getNoticeById(Long noticeId, Long userId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));
        
        NoticeDTO noticeDTO = new NoticeDTO(notice);
        
        // 좋아요 여부 확인
        boolean isLiked = likeService.isLikedByUser(userId, LikeType.NOTICE, noticeId);
        noticeDTO.setLiked(isLiked);
        
        return noticeDTO;
    }

    // 조회수 증가
    @Transactional
    public void incrementViews(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found"));
        notice.setView(notice.getView() + 1);
        noticeRepository.save(notice);
    }

    // 검색 및 정렬된 게시글 조회
    @Transactional(readOnly = true)
    public Page<NoticeDTO> getNotices(String search, String sort, Long domainId, NoticeType noticeType, Pageable pageable) {
        Page<Notice> notices;
        
        if (search != null && !search.isEmpty()) {
            notices = noticeRepository.searchByTitleOrContent(search, ContentStatus.ACTIVE, pageable);
        } else if (domainId != null && noticeType != null) {
            notices = noticeRepository.findByDomain_DomainIdAndNoticeTypeAndContentStatusOrderByCreatedAtDesc(
                domainId, noticeType, ContentStatus.ACTIVE, pageable);
        } else if (domainId != null) {
            notices = noticeRepository.findByDomain_DomainIdAndContentStatusOrderByCreatedAtDesc(
                domainId, ContentStatus.ACTIVE, pageable);
        } else if (noticeType != null) {
            notices = noticeRepository.findByNoticeTypeAndContentStatusOrderByCreatedAtDesc(
                noticeType, ContentStatus.ACTIVE, pageable);
        } else if ("view".equals(sort)) {
            notices = noticeRepository.findAllOrderByViewDesc(ContentStatus.ACTIVE, pageable);
        } else if ("likes".equals(sort)) {
            notices = noticeRepository.findAllOrderByLikesDesc(ContentStatus.ACTIVE, pageable);
        } else {
            notices = noticeRepository.findByContentStatusOrderByCreatedAtDesc(ContentStatus.ACTIVE, pageable);
        }
        
        return notices.map(NoticeDTO::new);
    }

    // 내가 작성한 게시글 조회
    @Transactional(readOnly = true)
    public Page<NoticeDTO> getMyNotices(Long userId, NoticeType noticeType, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Notice> notices;
        
        if (noticeType != null) {
            notices = noticeRepository.findByUser_UserIdAndNoticeTypeAndContentStatusOrderByCreatedAtDesc(
                userId, noticeType, ContentStatus.ACTIVE, pageable);
        } else {
            notices = noticeRepository.findByUser_UserIdAndContentStatusOrderByCreatedAtDesc(
                userId, ContentStatus.ACTIVE, pageable);
        }
        
        return notices.map(NoticeDTO::new);
    }
} 