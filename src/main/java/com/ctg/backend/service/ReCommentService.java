package com.ctg.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctg.backend.dto.ReCommentDTO;
import com.ctg.backend.entity.Comment;
import com.ctg.backend.entity.LikeType;
import com.ctg.backend.entity.ReComment;
import com.ctg.backend.entity.User;
import com.ctg.backend.entity.UserRole;
import com.ctg.backend.repository.CommentRepository;
import com.ctg.backend.repository.LikeRepository;
import com.ctg.backend.repository.ReCommentRepository;
import com.ctg.backend.repository.UserRepository;
import com.ctg.backend.repository.UserUpdateRepository;
@Service
public class ReCommentService {
    
    @Autowired
    private ReCommentRepository reCommentRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUpdateRepository userUpdateRepository;

    public ReCommentService(ReCommentRepository reCommentRepository, CommentRepository commentRepository, LikeRepository likeRepository, UserRepository userRepository, UserUpdateRepository userUpdateRepository) {
        this.reCommentRepository = reCommentRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.userUpdateRepository = userUpdateRepository;
    }

    // 대댓글 생성
    public ReCommentDTO createReComment(ReCommentDTO reCommentDTO) {
        Comment comment = commentRepository.findById(reCommentDTO.getCommentId())
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + reCommentDTO.getCommentId()));

        User user = userRepository.findById(reCommentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + reCommentDTO.getUserId()));

        ReComment reComment = new ReComment();
        reComment.setComment(comment); // Comment 객체 설정
        reComment.setUser(user); // User 객체 설정
        reComment.setContent(reCommentDTO.getContent());
        reComment.setCreatedAt(java.time.LocalDateTime.now());
        reComment.setContentStatus(com.ctg.backend.entity.ContentStatus.ACTIVE);

        ReComment savedReComment = reCommentRepository.save(reComment);
        return convertToDTO(savedReComment, user.getUserId());
    }

    // 대댓글 수정
    public ReCommentDTO updateReComment(Long reCommentId, String content, Long userId) {
        ReComment reComment = reCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new RuntimeException("대댓글을 찾을 수 없습니다: " + reCommentId));

        // 작성자 본인만 수정 가능
        if (!reComment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("본인만 대댓글을 수정할 수 있습니다.");
        }

        reComment.setContent(content);
        reComment.setUpdatedAt(LocalDateTime.now());

        ReComment updatedReComment = reCommentRepository.save(reComment);
        return convertToDTO(updatedReComment, userId);
    }

    // 대댓글 삭제 (Soft Delete)
    public void deleteReComment(Long reCommentId, Long currentUserId) {
        ReComment reComment = reCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new RuntimeException("대댓글을 찾을 수 없습니다: " + reCommentId));

        if (!reComment.getUser().getUserId().equals(currentUserId)) {
            UserRole role = userUpdateRepository.findByUser_UserId(currentUserId)
                    .orElseThrow(() -> new IllegalArgumentException("권한 정보를 찾을 수 없습니다."))
                    .getRole();

            if (!role.canDeleteOthersComment()) {
                throw new SecurityException("타인의 대댓글을 삭제할 권한이 없습니다.");
            }
        }

        reComment.setContentStatus(com.ctg.backend.entity.ContentStatus.DELETED);
        reComment.setUpdatedAt(LocalDateTime.now());
        reCommentRepository.save(reComment);
    }

    // 특정 댓글의 대댓글 조회
    public List<ReCommentDTO> getReCommentsByCommentId(Long commentId, Long userId) {
        List<ReComment> reComments = reCommentRepository.findByComment_CommentIdAndContentStatus(commentId, com.ctg.backend.entity.ContentStatus.ACTIVE);

        return reComments.stream()
                .map(reComment -> {
                    ReCommentDTO reCommentDTO = convertToDTO(reComment, userId);

                    // 유저 정보 추가
                    User user = reComment.getUser();
                    reCommentDTO.setUserNickname(user.getNickname());
                    reCommentDTO.setUserProfileImage(user.getProfileImage());

                    return reCommentDTO;
                })
                .collect(Collectors.toList());
    }

    // 특정 사용자가 작성한 대댓글 조회
    public List<ReCommentDTO> getReCommentsByUserId(Long userId) {
        List<ReComment> reComments = reCommentRepository.findByUser_UserId(userId);
        return reComments.stream().map(re -> convertToDTO(re, userId)).collect(Collectors.toList());
    }

    // 대댓글 검색
    public List<ReCommentDTO> searchReComments(String keyword) {
        List<ReComment> reComments = reCommentRepository.findByContentContainingAndContentStatus(keyword, com.ctg.backend.entity.ContentStatus.ACTIVE);
        return reComments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ReCommentDTO convertToDTO(ReComment reComment) {
        User user = reComment.getUser();

        return new ReCommentDTO(
                reComment.getRecommentId(),
                user.getUserId(),
                reComment.getComment().getCommentId(),
                reComment.getContent(),
                reComment.getCreatedAt(),
                reComment.getUpdatedAt(),
                user.getNickname(),
                user.getProfileImage(),
                false, // 로그인 사용자 없으므로 기본값 false
                false
        );
    }

    // 엔티티 -> 디티오 변환
    private ReCommentDTO convertToDTO(ReComment reComment, Long userId) {
        User user = reComment.getUser();

        boolean isLiked = false;
        if (userId != null) {
            isLiked = likeRepository
                    .findByUser_UserIdAndLikeTypeAndReComment_RecommentId(userId, LikeType.RECOMMENT, reComment.getRecommentId())
                    .isPresent();
        }

        return new ReCommentDTO(
                reComment.getRecommentId(),
                user.getUserId(),
                reComment.getComment().getCommentId(),
                reComment.getContent(),
                reComment.getCreatedAt(),
                reComment.getUpdatedAt(),
                user.getNickname(),
                user.getProfileImage(),
                isLiked,
                false
        );
    }

}
