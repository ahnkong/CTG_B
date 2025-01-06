package com.hoolhool.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoolhool.backend.dto.CommentDTO;
import com.hoolhool.backend.dto.ReCommentDTO;
import com.hoolhool.backend.entity.Comment;
import com.hoolhool.backend.entity.ReComment;
import com.hoolhool.backend.repository.CommentRepository;
import com.hoolhool.backend.repository.LikeRepository;
import com.hoolhool.backend.repository.ReCommentRepository;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReCommentRepository reCommentRepository;

    @Autowired
    private LikeRepository likeRepository;



    // 댓글 생성
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setBoardId(commentDTO.getBoardId());
        comment.setUserId(commentDTO.getUserId());
        comment.setContent(commentDTO.getContent());
        comment.setCoCDate(LocalDateTime.now());
        comment.setCoLikes(0); // 초기 좋아요 수
    
        Comment savedComment = commentRepository.save(comment);
        return convertToDTOWithReComments(savedComment); // 대댓글 포함 변환 메서드 호출
    }

    // 댓글 수정
    public CommentDTO updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
    
        comment.setContent(content);
        comment.setCoCDate(LocalDateTime.now()); // 수정 시간 갱신
    
        Comment updatedComment = commentRepository.save(comment);
        return convertToDTOWithReComments(updatedComment); // 대댓글 포함 변환 메서드 호출
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        // 댓글 존재 여부 확인
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("댓글을 찾을 수 없습니다: " + commentId);
        }

        // 댓글에 연결된 좋아요 삭제
        likeRepository.deleteByCommentId(commentId);

        // 댓글 삭제
        commentRepository.deleteById(commentId);
    }

    // 특정 게시글의 댓글 조회 (대댓글 포함)
    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return comments.stream()
                .map(this::convertToDTOWithReComments) // 대댓글 포함 변환 메서드 호출
                .collect(Collectors.toList());
    }

    // 특정 사용자가 작성한 댓글 조회
    public List<CommentDTO> getCommentsByUserId(String userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(this::convertToDTOWithReComments) // 대댓글 포함 변환 메서드 호출
                .collect(Collectors.toList());
    }

    // 댓글 검색
    public List<CommentDTO> searchComments(String keyword) {
        List<Comment> comments = commentRepository.searchCommentsByKeyword(keyword);
        return comments.stream()
                .map(this::convertToDTOWithReComments) // 대댓글 포함 변환 메서드 호출
                .collect(Collectors.toList());
    }

    // 엔티티 -> 디티오 변환
    private CommentDTO convertToDTOWithReComments(Comment comment) {
        // 댓글에 속한 대댓글 조회
        List<ReComment> reComments = reCommentRepository.findByCommentId(comment.getCommentId());
        List<ReCommentDTO> reCommentDTOs = reComments.stream()
                .map(this::convertReCommentToDTO)
                .collect(Collectors.toList());
    
        // 댓글 DTO 생성 및 대댓글 포함
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setBoardId(comment.getBoardId());
        commentDTO.setUserId(comment.getUserId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCoCDate(comment.getCoCDate());
        commentDTO.setCoLikes(comment.getCoLikes());
        commentDTO.setReComments(reCommentDTOs); // 대댓글 포함
        return commentDTO;
    }

    // 대댓글 엔티티 -> 디티오 변환
    private ReCommentDTO convertReCommentToDTO(ReComment reComment) {
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
