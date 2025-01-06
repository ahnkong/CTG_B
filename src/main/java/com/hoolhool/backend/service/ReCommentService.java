package com.hoolhool.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoolhool.backend.dto.ReCommentDTO;
import com.hoolhool.backend.entity.ReComment;
import com.hoolhool.backend.repository.LikeRepository;
import com.hoolhool.backend.repository.ReCommentRepository;

@Service
public class ReCommentService {
    
    @Autowired
    private ReCommentRepository reCommentRepository;

    @Autowired
    private LikeRepository likeRepository;

    // 대댓글 생성
    public ReCommentDTO createReComment(ReCommentDTO reCommentDTO) {
        ReComment reComment = new ReComment();
        reComment.setCommentId(reCommentDTO.getCommentId());
        reComment.setUserId(reCommentDTO.getUserId());
        reComment.setContent(reCommentDTO.getContent());
        reComment.setReCDate(LocalDateTime.now());
        reComment.setReLikes(0); // 초기 좋아요 수

        ReComment savedReComment = reCommentRepository.save(reComment);
        return convertToDTO(savedReComment);
    }

    // 대댓글 수정
    public ReCommentDTO updateReComment(Long reCommentId, String content) {
        ReComment reComment = reCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new RuntimeException("대댓글을 찾을 수 없습니다: " + reCommentId));

        reComment.setContent(content);
        reComment.setReCDate(LocalDateTime.now());

        ReComment updatedReComment = reCommentRepository.save(reComment);
        return convertToDTO(updatedReComment);
    }

    // 대댓글 삭제
    public void deleteReComment(Long reCommentId) {
        // 대댓글 존재 여부 확인
        if (!reCommentRepository.existsById(reCommentId)) {
            throw new RuntimeException("대댓글을 찾을 수 없습니다: " + reCommentId);
        }

        // 대댓글에 연결된 좋아요 삭제
        likeRepository.deleteByRecommentId(reCommentId);

        // 대댓글 삭제
        reCommentRepository.deleteById(reCommentId);
    }

    // 특정 댓글의 대댓글 조회
    public List<ReCommentDTO> getReCommentsByCommentId(Long commentId) {
        List<ReComment> reComments = reCommentRepository.findByCommentId(commentId);
        return reComments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // 특정 사용자가 작성한 대댓글 조회
    public List<ReCommentDTO> getReCommentsByUserId(String userId) {
        List<ReComment> reComments = reCommentRepository.findByUserId(userId);
        return reComments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 대댓글 검색
    public List<ReCommentDTO> searchReComments(String keyword) {
        List<ReComment> reComments = reCommentRepository.findByContentContaining(keyword);
        return reComments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 엔티티 -> 디티오 변환
    private ReCommentDTO convertToDTO(ReComment reComment) {
        return new ReCommentDTO(
                reComment.getRecommentId(),
                reComment.getCommentId(),
                reComment.getUserId(),
                reComment.getContent(),
                reComment.getReCDate(),
                reComment.getReLikes()
        );
    }

}
