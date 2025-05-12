package com.ctg.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctg.backend.dto.CommentDTO;
import com.ctg.backend.dto.MyCommentDTO;
import com.ctg.backend.dto.ReCommentDTO;
import com.ctg.backend.entity.Comment;
import com.ctg.backend.entity.ContentStatus;
import com.ctg.backend.entity.LikeType;
import com.ctg.backend.entity.ReComment;
import com.ctg.backend.entity.User;
import com.ctg.backend.entity.BoardType;
// import com.ctg.backend.repository.BoardRepository;
import com.ctg.backend.repository.CommentRepository;
import com.ctg.backend.repository.LikeRepository;
import com.ctg.backend.repository.ReCommentRepository;
import com.ctg.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    // @Autowired
    // private BoardRepository boardRepository;

    @Autowired
    private ReCommentRepository reCommentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    // public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, 
    //                       LikeRepository likeRepository, ReCommentRepository reCommentRepository, UserRepository userRepository) {
    //     this.commentRepository = commentRepository;
    //     this.boardRepository = boardRepository;
    //     this.likeRepository = likeRepository;
    //     this.reCommentRepository = reCommentRepository;
    //     this.userRepository = userRepository;
    // }
    public CommentService(CommentRepository commentRepository, LikeRepository likeRepository, ReCommentRepository reCommentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.reCommentRepository = reCommentRepository;
        this.userRepository = userRepository;
    }

    // 댓글 생성
    public CommentDTO createComment(CommentDTO commentDTO, Long userId) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setBoardType(commentDTO.getBoardType());
        comment.setBoardId(commentDTO.getBoardId());
        comment.setContentStatus(ContentStatus.ACTIVE);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        comment.setUser(user);
    
        Comment savedComment = commentRepository.save(comment);
        return new CommentDTO(savedComment);
    }

    // 댓글 수정
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

        // 게시판 타입과 ID가 일치하는지 확인
        if (!comment.getBoardType().equals(commentDTO.getBoardType()) || 
            !comment.getBoardId().equals(commentDTO.getBoardId())) {
            throw new IllegalArgumentException("잘못된 게시글 정보입니다.");
        }

        // 내용이 변경되었는지 확인
        boolean isEdited = !comment.getContent().equals(commentDTO.getContent());
        comment.setContent(commentDTO.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment updatedComment = commentRepository.save(comment);
        CommentDTO dto = new CommentDTO(updatedComment);
        return dto;
    }

    // 댓글 삭제 (ContentStatus를 DELETED로 변경)
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

        // 이미 삭제된 댓글인지 확인
        if (comment.getContentStatus() == ContentStatus.DELETED) {
            throw new IllegalArgumentException("이미 삭제된 댓글입니다.");
        }

        // ContentStatus를 DELETED로 변경
        comment.setContentStatus(ContentStatus.DELETED);
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    // 특정 게시글의 댓글 조회 (대댓글 포함)
    public List<CommentDTO> getCommentsByBoard(BoardType boardType, Long boardId, Long userId) {
        // 활성화된 댓글만 조회
        List<Comment> comments = commentRepository.findActiveCommentsByBoardTypeAndBoardId(boardType, boardId);

        return comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO(comment);

                    // 유저 정보 추가
                    User user = comment.getUser();
                    commentDTO.setUserNickname(user.getNickname()); 
                    commentDTO.setUserProfileImage(user.getProfileImage());

                    // 댓글 좋아요 여부 추가
                    boolean isLiked = likeRepository
                            .findByUser_UserIdAndLikeTypeAndComment_CommentId(userId, LikeType.COMMENT, comment.getCommentId())
                            .isPresent();
                    commentDTO.setLiked(isLiked);

                    // 대댓글 리스트 설정
                List<ReCommentDTO> reDTOs = comment.getReComments().stream()
                            .filter(reComment -> reComment.getContentStatus() == ContentStatus.ACTIVE)
                            .map(re -> convertReCommentToDTO(re, userId))
                .collect(Collectors.toList());
                    commentDTO.setReComments(reDTOs);

                    return commentDTO;
                })
                .collect(Collectors.toList());
    }

    // 특정 사용자가 작성한 댓글 조회
    public List<CommentDTO> getCommentsByUserId(Long userId) {
        // 사용자가 작성한 댓글 조회 (대댓글 포함)
        List<Comment> comments = commentRepository.findActiveCommentsByUserId(userId);
        
        return comments.stream()
                .map(comment -> {
                    CommentDTO dto = new CommentDTO(comment);
                    
                    // 유저 정보 추가
                    User user = comment.getUser();
                    dto.setUserNickname(user.getNickname());
                    dto.setUserProfileImage(user.getProfileImage());
                    
                    // 댓글 좋아요 여부 추가
                    boolean isLiked = likeRepository
                            .findByUser_UserIdAndLikeTypeAndComment_CommentId(userId, LikeType.COMMENT, comment.getCommentId())
                            .isPresent();
                    dto.setLiked(isLiked);
                    
                    // 대댓글 리스트 설정 (활성화된 대댓글만)
                    List<ReCommentDTO> reDTOs = comment.getReComments().stream()
                            .filter(reComment -> reComment.getContentStatus() == ContentStatus.ACTIVE)
                            .map(re -> convertReCommentToDTO(re, userId))
                            .collect(Collectors.toList());
                    dto.setReComments(reDTOs);
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 댓글 검색
    public List<CommentDTO> searchComments(String keyword) {
        // 활성화된 댓글만 검색
        List<Comment> comments = commentRepository.searchActiveCommentsByKeyword(keyword);
        
        return comments.stream()
                .map(comment -> {
                    CommentDTO dto = new CommentDTO(comment);
                    
                    // 유저 정보 추가
                    User user = comment.getUser();
                    dto.setUserNickname(user.getNickname());
                    dto.setUserProfileImage(user.getProfileImage());
                    
                    // 대댓글 리스트 설정 (활성화된 대댓글만)
                    List<ReCommentDTO> reDTOs = comment.getReComments().stream()
                            .filter(reComment -> reComment.getContentStatus() == ContentStatus.ACTIVE)
                            .map(re -> convertReCommentToDTO(re, null))  // 검색에서는 좋아요 상태를 확인하지 않음
                            .collect(Collectors.toList());
                    dto.setReComments(reDTOs);
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 댓글 수 + 대댓글 수 전체 카운트 반환
    public long countTotalComments(BoardType boardType, Long boardId) {
        // 활성화된 댓글 수
        long commentCount = commentRepository.countCommentsByBoardTypeAndBoardId(boardType, boardId);
        
        // 활성화된 대댓글 수
        long reCommentCount = reCommentRepository.findByComment_CommentId(boardId).stream()
                .filter(reComment -> reComment.getContentStatus() == ContentStatus.ACTIVE)
                .count();
                
        return commentCount + reCommentCount; // 댓글 + 대댓글 개수 합산
    }

    // 🔹 댓글 변환 메서드 (userId 없이도 가능, 내부적으로 userId null로 넘김)
    private CommentDTO convertToDTOWithReComments(Comment comment) {
        return convertToDTOWithReComments(comment, null);
    }

    // 엔티티 -> 디티오 변환
    private CommentDTO convertToDTOWithReComments(Comment comment, Long userId) {
        // 1. 대댓글 변환
        List<ReCommentDTO> reCommentDTOs = reCommentRepository.findByComment_CommentId(comment.getCommentId()).stream()
                .map(re -> convertReCommentToDTO(re, userId))
                .collect(Collectors.toList());

        // 2. 기본 댓글 정보 설정
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setBoardId(comment.getBoardId());
        dto.setBoardType(comment.getBoardType());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setReComments(reCommentDTOs);
        dto.setLiked(false); // 기본값 false

        // 3. 유저 정보 세팅
        User user = comment.getUser();
        dto.setUserNickname(user.getNickname());
        dto.setUserProfileImage(user.getProfileImage());

        // 4. 좋아요 상태 세팅 (userId 있을 때만)
        if (userId != null) {
            boolean isLiked = likeRepository
                    .findByUser_UserIdAndLikeTypeAndComment_CommentId(userId, LikeType.COMMENT, comment.getCommentId())
                    .isPresent();
            dto.setLiked(isLiked);
        }

        return dto;
    }

    // 대댓글 엔티티 -> 디티오 변환
    private ReCommentDTO convertReCommentToDTO(ReComment reComment, Long userId) {
        User user = reComment.getUser();
        boolean isLiked = likeRepository
                .findByUser_UserIdAndLikeTypeAndReComment_RecommentId(userId, LikeType.RECOMMENT, reComment.getRecommentId())
                .isPresent();

        ReCommentDTO dto = new ReCommentDTO();
        dto.setRecommentId(reComment.getRecommentId());
        dto.setUserId(reComment.getUser().getUserId());
        dto.setCommentId(reComment.getComment().getCommentId());
        dto.setContent(reComment.getContent());
        dto.setCreatedAt(reComment.getCreatedAt());
        dto.setUserNickname(user.getNickname());
        dto.setUserProfileImage(user.getProfileImage());
        dto.setLiked(isLiked);
        
        return dto;
    }

    // DTO -> 엔티티 변환
    private Comment convertToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setCommentId(commentDTO.getCommentId());
        comment.setContent(commentDTO.getContent());
        comment.setBoardType(commentDTO.getBoardType());
        comment.setBoardId(commentDTO.getBoardId());
        comment.setContentStatus(ContentStatus.ACTIVE);
        comment.setCreatedAt(LocalDateTime.now());
        
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        comment.setUser(user);
        
        return comment;
    }

    // CommentService.java

    public List<MyCommentDTO> getMyComments(Long userId) {
        List<Comment> comments = commentRepository.findActiveCommentsByUserId(userId);
        return comments.stream()
                       .map(this::convertToMyCommentDTO)
                       .collect(Collectors.toList());
    }

    private MyCommentDTO convertToMyCommentDTO(Comment comment) {
        return MyCommentDTO.builder()
            .commentId(comment.getCommentId())
            .boardId(comment.getBoardId())
            .content(comment.getContent())
            .createDate(comment.getCreatedAt())
            .build();
    }

}
