package com.hoolhool.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoolhool.backend.dto.CommentDTO;
import com.hoolhool.backend.dto.ReCommentDTO;
import com.hoolhool.backend.entity.Board;
import com.hoolhool.backend.entity.Comment;
import com.hoolhool.backend.entity.LikeType;
import com.hoolhool.backend.entity.ReComment;
import com.hoolhool.backend.entity.User;
import com.hoolhool.backend.repository.BoardRepository;
import com.hoolhool.backend.repository.CommentRepository;
import com.hoolhool.backend.repository.LikeRepository;
import com.hoolhool.backend.repository.ReCommentRepository;
import com.hoolhool.backend.repository.UserRepository;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReCommentRepository reCommentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository, 
                          LikeRepository likeRepository, ReCommentRepository reCommentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.likeRepository = likeRepository;
        this.reCommentRepository = reCommentRepository;
        this.userRepository = userRepository;
    }

    // 댓글 생성
    public CommentDTO createComment(CommentDTO commentDTO) {
        Board board = boardRepository.findById(commentDTO.getBoardId())
                        .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다 : " + commentDTO.getBoardId()));

        Comment comment = new Comment();
        comment.setBoard(board); 
        comment.setUserId(commentDTO.getUserId());
        comment.setContent(commentDTO.getContent());
        comment.setCoCDate(LocalDateTime.now());
    
        Comment savedComment = commentRepository.save(comment);
        return convertToDTOWithReComments(savedComment, commentDTO.getUserId());
    }

    // 댓글 수정
    public CommentDTO updateComment(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));

        boolean isEdited = !comment.getContent().equals(newContent); // ✅ 수정됐는지 확인
        comment.setContent(newContent);

        Comment updatedComment = commentRepository.save(comment);

        CommentDTO dto = convertToDTOWithReComments(updatedComment, comment.getUserId());
        dto.setEdited(isEdited); // ✅ 수정 여부만 DTO에 담기
        return dto;
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        // 댓글 존재 여부 확인
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("댓글을 찾을 수 없습니다: " + commentId);
        }

        // 댓글에 연결된 좋아요 삭제
        likeRepository.deleteByComment_CommentId(commentId);

        // 댓글 삭제
        commentRepository.deleteById(commentId);
    }

    // 특정 게시글의 댓글 조회 (대댓글 포함)
    public List<CommentDTO> getCommentsByBoardId(Long boardId, String userId) {
        List<Comment> comments = commentRepository.findByBoard_BoardId(boardId);

        return comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = convertToDTOWithReComments(comment, comment.getUserId());

                    // 유저 정보 추가
                    User user = userRepository.findById(comment.getUserId())
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + comment.getUserId()));

                    commentDTO.setUserNickname(user.getNickname()); 
                    commentDTO.setUserProfileImage(user.getProfileImage());

                    // ✅ 댓글 좋아요 여부 추가
                    boolean isLiked = likeRepository
                            .findByUser_UserIdAndTypeAndComment_CommentId(userId, LikeType.COMMENT,
                                    comment.getCommentId())
                            .isPresent();
                    commentDTO.setLiked(isLiked);

                    // ✅ 대댓글 리스트 설정 (대댓글의 isLiked 값 추가)
                    List<ReCommentDTO> reDTOs = comment.getReComments().stream()
                            .map(re -> {
                                boolean reLiked = likeRepository
                                        .findByUser_UserIdAndTypeAndReComment_RecommentId(userId, LikeType.RECOMMENT,
                                                re.getRecommentId())
                                        .isPresent();

                                return new ReCommentDTO(
                                        re.getRecommentId(),
                                        re.getUserId(),
                                        re.getComment().getCommentId(),
                                        re.getContent(),
                                        re.getReCDate(),
                                        user.getNickname(),
                                        user.getProfileImage(),
                                        reLiked, // ✅ 대댓글 좋아요 상태 추가
                                        false
                                );
                            })
                            .collect(Collectors.toList());

                    commentDTO.setReComments(reDTOs);

                    return commentDTO;
                })
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

    // 댓글 수 + 대댓글 수 전체 카운트 반환
    public long countTotalComments(Long boardId) {
        long commentCount = commentRepository.countCommentsByBoardId(boardId);
        long reCommentCount = reCommentRepository.countReCommentsByBoardId(boardId);
        return commentCount + reCommentCount; // 댓글 + 대댓글 개수 합산
    }

    // 🔹 댓글 변환 메서드 (userId 없이도 가능, 내부적으로 userId null로 넘김)
    private CommentDTO convertToDTOWithReComments(Comment comment) {
        return convertToDTOWithReComments(comment, null); // 오버로딩된 메서드 호출
    }

    // 엔티티 -> 디티오 변환
    private CommentDTO convertToDTOWithReComments(Comment comment, String userId) {
        // 1. 대댓글 변환
        List<ReCommentDTO> reCommentDTOs = reCommentRepository.findByComment_CommentId(comment.getCommentId()).stream()
                .map(re -> convertReCommentToDTO(re, userId))
                .collect(Collectors.toList());

        // 2. 기본 댓글 정보 설정
        CommentDTO dto = new CommentDTO(
                comment.getCommentId(),
                comment.getUserId(),
                comment.getBoard().getBoardId(),
                comment.getContent(),
                comment.getCoCDate(),
                reCommentDTOs,
                false, // isLiked 기본값 false
                false
        );

        // 3. 유저 정보 세팅
        User user = userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + comment.getUserId()));
        dto.setUserNickname(user.getNickname());
        dto.setUserProfileImage(user.getProfileImage());

        // 4. 좋아요 상태 세팅 (userId 있을 때만)
        if (userId != null) {
            boolean isLiked = likeRepository
                    .findByUser_UserIdAndTypeAndComment_CommentId(userId, LikeType.COMMENT, comment.getCommentId())
                    .isPresent();
            dto.setLiked(isLiked);
        }

        return dto;
    }

    // 대댓글 엔티티 -> 디티오 변환
    private ReCommentDTO convertReCommentToDTO(ReComment reComment, String userId) {
        User user = userRepository.findById(reComment.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + reComment.getUserId()));

        boolean isLiked = likeRepository
                .findByUser_UserIdAndTypeAndReComment_RecommentId(userId, LikeType.RECOMMENT,
                        reComment.getRecommentId())
                .isPresent();

        return new ReCommentDTO(
            reComment.getRecommentId(),
            reComment.getUserId(),
            reComment.getComment().getCommentId(), // 대댓글이 속한 댓글 ID 저장
            reComment.getContent(),
            reComment.getReCDate(),
            user.getNickname(),
            user.getProfileImage(),
            isLiked,
            false
        );
    }

    // 🔹 DTO -> 엔티티 변환 (Board 객체 설정)
    private Comment convertToEntity(CommentDTO commentDTO) {
        Board board = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + commentDTO.getBoardId()));

        return new Comment(
                commentDTO.getCommentId(),
                board, // Board 객체 설정
                commentDTO.getUserId(),
                commentDTO.getContent(),
                commentDTO.getCoCDate()
        );
    }

    
}
