package com.hoolhool.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoolhool.backend.entity.Board;
import com.hoolhool.backend.entity.Comment;
import com.hoolhool.backend.entity.Like;
import com.hoolhool.backend.entity.LikeType;
import com.hoolhool.backend.entity.ReComment;
import com.hoolhool.backend.entity.User;
import com.hoolhool.backend.repository.BoardRepository;
import com.hoolhool.backend.repository.CommentRepository;
import com.hoolhool.backend.repository.LikeRepository;
import com.hoolhool.backend.repository.ReCommentRepository;
import com.hoolhool.backend.repository.UserRepository;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReCommentRepository reCommentRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, BoardRepository boardRepository,
                       CommentRepository commentRepository, ReCommentRepository reCommentRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
        this.reCommentRepository = reCommentRepository;
    }

    // 좋아요 클릭, 취소
    @Transactional
    public String toggleLike(String userId, LikeType type, Long id) { // type을 LikeType으로 변경
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        Optional<Like> existingLike = Optional.empty();

        switch (type) {
            case BOARD:
                existingLike = likeRepository.findByUser_UserIdAndTypeAndBoard_BoardId(userId, type, id);
                break;
            case COMMENT:
                existingLike = likeRepository.findByUser_UserIdAndTypeAndComment_CommentId(userId, type, id);
                break;
            case RECOMMENT:
                existingLike = likeRepository.findByUser_UserIdAndTypeAndReComment_RecommentId(userId, type, id);
                break;
            default:
                throw new IllegalArgumentException("잘못된 LikeType: " + type);
        }

        if (existingLike.isPresent()) {
            switch (type) {
                case BOARD:
                    likeRepository.deleteByUser_UserIdAndTypeAndBoard_BoardId(userId, type, id);
                    break;
                case COMMENT:
                    likeRepository.deleteByUser_UserIdAndTypeAndComment_CommentId(userId, type, id);
                    break;
                case RECOMMENT:
                    likeRepository.deleteByUser_UserIdAndTypeAndReComment_RecommentId(userId, type, id);
                    break;
            }
            return "좋아요 취소";
        } else {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setType(type);
            newLike.setLikeDate(LocalDateTime.now());

            switch (type) {
                case BOARD:
                    Board board = boardRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + id));
                    newLike.setBoard(board);
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
    public long countLikes(LikeType type, Long id) { // type을 LikeType으로 변경
        switch (type) {
            case BOARD:
                return likeRepository.countByTypeAndBoard_BoardId(type, id);
            case COMMENT:
                return likeRepository.countByTypeAndComment_CommentId(type, id);
            case RECOMMENT:
                return likeRepository.countByTypeAndReComment_RecommentId(type, id);
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    // 좋아요 여부 확인
    public boolean isLikedByUser(String userId, LikeType type, Long id) { // type을 LikeType으로 변경
        switch (type) {
            case BOARD:
                return likeRepository.findByUser_UserIdAndTypeAndBoard_BoardId(userId, type, id).isPresent();
            case COMMENT:
                return likeRepository.findByUser_UserIdAndTypeAndComment_CommentId(userId, type, id).isPresent();
            case RECOMMENT:
                return likeRepository.findByUser_UserIdAndTypeAndReComment_RecommentId(userId, type, id).isPresent();
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

}
