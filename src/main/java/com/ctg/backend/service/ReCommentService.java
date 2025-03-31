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
import com.ctg.backend.repository.CommentRepository;
import com.ctg.backend.repository.LikeRepository;
import com.ctg.backend.repository.ReCommentRepository;
import com.ctg.backend.repository.UserRepository;

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

    public ReCommentService(ReCommentRepository reCommentRepository, CommentRepository commentRepository, LikeRepository likeRepository, UserRepository userRepository) {
        this.reCommentRepository = reCommentRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    // 대댓글 생성
    public ReCommentDTO createReComment(ReCommentDTO reCommentDTO) {
        Comment comment = commentRepository.findById(reCommentDTO.getCommentId())
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + reCommentDTO.getCommentId()));

        ReComment reComment = new ReComment();
        reComment.setComment(comment); // Comment 객체 설정
        reComment.setUserId(reCommentDTO.getUserId());
        reComment.setContent(reCommentDTO.getContent());
        reComment.setReCDate(LocalDateTime.now());

        ReComment savedReComment = reCommentRepository.save(reComment);
        return convertToDTO(savedReComment, reCommentDTO.getUserId());
    }

    // 대댓글 수정
    public ReCommentDTO updateReComment(Long reCommentId, String content, String userId) {
        ReComment reComment = reCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new RuntimeException("대댓글을 찾을 수 없습니다: " + reCommentId));

        reComment.setContent(content);
        reComment.setReCDate(LocalDateTime.now());

        ReComment updatedReComment = reCommentRepository.save(reComment);
        return convertToDTO(updatedReComment, userId);
    }

    // 대댓글 삭제
    public void deleteReComment(Long reCommentId) {
        // 대댓글 존재 여부 확인
        if (!reCommentRepository.existsById(reCommentId)) {
            throw new RuntimeException("대댓글을 찾을 수 없습니다: " + reCommentId);
        }

        // 대댓글에 연결된 좋아요 삭제
        likeRepository.deleteByReComment_RecommentId(reCommentId);

        // 대댓글 삭제
        reCommentRepository.deleteById(reCommentId);
    }

    // 특정 댓글의 대댓글 조회
    public List<ReCommentDTO> getReCommentsByCommentId(Long commentId, String userId) {
        List<ReComment> reComments = reCommentRepository.findByComment_CommentId(commentId);

        return reComments.stream()
                .map(reComment -> {
                    ReCommentDTO reCommentDTO = convertToDTO(reComment, userId);

                    // 유저 정보 추가
                    User user = userRepository.findById(reComment.getUserId())
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + reComment.getUserId()));

                    reCommentDTO.setUserNickname(user.getNickname());
                    reCommentDTO.setUserProfileImage(user.getProfileImage());

                    return reCommentDTO;
                })
                .collect(Collectors.toList());
    }

    // 특정 사용자가 작성한 대댓글 조회
    public List<ReCommentDTO> getReCommentsByUserId(String userId) {
        List<ReComment> reComments = reCommentRepository.findByUserId(userId);
        return reComments.stream().map(re -> convertToDTO(re, userId)).collect(Collectors.toList());
    }

    // 대댓글 검색
    public List<ReCommentDTO> searchReComments(String keyword) {
        List<ReComment> reComments = reCommentRepository.findByContentContaining(keyword);
        return reComments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ReCommentDTO convertToDTO(ReComment reComment) {
        User user = userRepository.findById(reComment.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + reComment.getUserId()));

        return new ReCommentDTO(
                reComment.getRecommentId(),
                reComment.getUserId(),
                reComment.getComment().getCommentId(),
                reComment.getContent(),
                reComment.getReCDate(),
                user.getNickname(),
                user.getProfileImage(),
                false, // 로그인 사용자 없으므로 기본값 false
                false
        );
    }

    // 엔티티 -> 디티오 변환
    private ReCommentDTO convertToDTO(ReComment reComment, String userId) {
        User user = userRepository.findById(reComment.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + reComment.getUserId()));

        boolean isLiked = likeRepository
                .findByUser_UserIdAndTypeAndReComment_RecommentId(userId, LikeType.RECOMMENT,
                        reComment.getRecommentId())
                .isPresent();

        return new ReCommentDTO(
                reComment.getRecommentId(),
                reComment.getUserId(),
                reComment.getComment().getCommentId(),
                reComment.getContent(),
                reComment.getReCDate(),
                user.getNickname(),
                user.getProfileImage(),
                isLiked,
                false
        );
    }

}
