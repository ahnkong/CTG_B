package com.ctg.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctg.backend.entity.Comment;
import com.ctg.backend.entity.Like;
import com.ctg.backend.entity.LikeType;
import com.ctg.backend.entity.ReComment;
import com.ctg.backend.entity.User;
import com.ctg.backend.repository.CommentRepository;
import com.ctg.backend.repository.LikeRepository;
import com.ctg.backend.repository.ReCommentRepository;
import com.ctg.backend.repository.UserRepository;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReCommentRepository reCommentRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository,
                       CommentRepository commentRepository, ReCommentRepository reCommentRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.reCommentRepository = reCommentRepository;
    }

    // 좋아요 클릭, 취소
    @Transactional
    public String toggleLike(Long userId, LikeType type, Long id) {
        // 1. 사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // 2. 기본 좋아요 여부 확인
        Optional<Like> existingLike = Optional.empty();
        switch (type) {
            case COMMUNITY:
            case NOTICE:
            case VIDEO:
                existingLike = likeRepository.findByUser_UserIdAndLikeTypeAndTargetId(userId, type, id);
                break;
            case COMMENT:
                existingLike = likeRepository.findByUser_UserIdAndLikeTypeAndComment_CommentId(userId, type, id);
                break;
            case RECOMMENT:
                existingLike = likeRepository.findByUser_UserIdAndLikeTypeAndReComment_RecommentId(userId, type, id);
                break;
            default:
                throw new IllegalArgumentException("잘못된 LikeType: " + type);
        }

        // 3. 기존 좋아요가 있는경우 삭제
        if (existingLike.isPresent()) {
            switch (type) {
                case COMMUNITY:
                case NOTICE:
                case VIDEO:
                    likeRepository.deleteByUser_UserIdAndLikeTypeAndTargetId(userId, type, id);
                    break;
                case COMMENT:
                    likeRepository.deleteByUser_UserIdAndLikeTypeAndComment_CommentId(userId, type, id);
                    break;
                case RECOMMENT:
                    likeRepository.deleteByUser_UserIdAndLikeTypeAndReComment_RecommentId(userId, type, id);
                    break;
            }
            return "좋아요 취소";
        } else {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setLikeType(type);
            newLike.setCreatedAt(LocalDateTime.now());
            newLike.setTargetId(id);

            switch (type) {
                case COMMUNITY:
                case NOTICE:
                case VIDEO:
                    // 게시판 타입의 경우 targetId만 설정
                    break;
                case COMMENT:
                    Comment comment = commentRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + id));
                    newLike.setComment(comment);
                    break;
                case RECOMMENT:
                    ReComment reComment = reCommentRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("대댓글을 찾을 수 없습니다: " + id));
                    newLike.setReComment(reComment);
                    break;
            }

            likeRepository.save(newLike);
            return "좋아요";
        }
    }

    // 좋아요 개수 조회
    public long countLikes(LikeType type, Long id) {
        switch (type) {
            case COMMUNITY:
            case NOTICE:
            case VIDEO:
                return likeRepository.countByLikeTypeAndTargetId(type, id);
            case COMMENT:
                return likeRepository.countByLikeTypeAndComment_CommentId(type, id);
            case RECOMMENT:
                return likeRepository.countByLikeTypeAndReComment_RecommentId(type, id);
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    // 좋아요 여부 확인
    public boolean isLikedByUser(Long userId, LikeType type, Long id) {
        switch (type) {
            case COMMUNITY:
            case NOTICE:
            case VIDEO:
                return likeRepository.findByUser_UserIdAndLikeTypeAndTargetId(userId, type, id).isPresent();
            case COMMENT:
                return likeRepository.findByUser_UserIdAndLikeTypeAndComment_CommentId(userId, type, id).isPresent();
            case RECOMMENT:
                return likeRepository.findByUser_UserIdAndLikeTypeAndReComment_RecommentId(userId, type, id).isPresent();
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    @Transactional
    public void selectBestComment(Long boardId, Long commentId, Long userId) {
        // 댓글 존재 확인
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));

        // 댓글 작성자의 ID 가져오기
        Long commentWriterId = comment.getUser().getUserId();

        // 게시글 작성자 (User 객체) 가져오기
        User boardWriter = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Like 엔티티 생성 (채택된 댓글 정보 저장)
        Like bestCommentLike = new Like();
        bestCommentLike.setUser(boardWriter);
        bestCommentLike.setTargetId(commentWriterId);
        bestCommentLike.setLikeType(LikeType.COMMENT);
        bestCommentLike.setComment(comment);
        bestCommentLike.setCreatedAt(LocalDateTime.now());

        likeRepository.save(bestCommentLike);
    }

}
