package com.hoolhool.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoolhool.backend.entity.Like;
import com.hoolhool.backend.entity.LikeType;
import com.hoolhool.backend.repository.LikeRepository;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    // 좋아요 클릭, 취소
    @Transactional
    public String toggleLike(String userId, LikeType type, Long id) { // type을 LikeType으로 변경
        Optional<Like> existingLike = null;

        switch (type) {
            case BOARD:
                existingLike = likeRepository.findByUserIdAndTypeAndBoardId(userId, type, id);
                break;
            case COMMENT:
                existingLike = likeRepository.findByUserIdAndTypeAndCommentId(userId, type, id);
                break;
            case RECOMMENT:
                existingLike = likeRepository.findByUserIdAndTypeAndRecommentId(userId, type, id);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        if (existingLike != null && existingLike.isPresent()) {
            switch (type) {
                case BOARD:
                    likeRepository.deleteByUserIdAndTypeAndBoardId(userId, type, id);
                    break;
                case COMMENT:
                    likeRepository.deleteByUserIdAndTypeAndCommentId(userId, type, id);
                    break;
                case RECOMMENT:
                    likeRepository.deleteByUserIdAndTypeAndRecommentId(userId, type, id);
                    break;
            }
            return "좋아요 취소";
        } else {
            Like newLike = new Like();
            newLike.setUserId(userId);
            newLike.setType(type); // LikeType Enum 사용
            newLike.setLikeDate(LocalDateTime.now());
            if (type == LikeType.BOARD) {
                newLike.setBoardId(id);
            } else if (type == LikeType.COMMENT) {
                newLike.setCommentId(id);
            } else if (type == LikeType.RECOMMENT) {
                newLike.setRecommentId(id);
            }
            likeRepository.save(newLike);
            return "좋아요";
        }
    }

    // 좋아요 개수 조회
    public long countLikes(LikeType type, Long id) { // type을 LikeType으로 변경
        switch (type) {
            case BOARD:
                return likeRepository.countByTypeAndBoardId(type, id);
            case COMMENT:
                return likeRepository.countByTypeAndCommentId(type, id);
            case RECOMMENT:
                return likeRepository.countByTypeAndRecommentId(type, id);
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    // 좋아요 여부 확인
    public boolean isLikedByUser(String userId, LikeType type, Long id) { // type을 LikeType으로 변경
        switch (type) {
            case BOARD:
                return likeRepository.findByUserIdAndTypeAndBoardId(userId, type, id).isPresent();
            case COMMENT:
                return likeRepository.findByUserIdAndTypeAndCommentId(userId, type, id).isPresent();
            case RECOMMENT:
                return likeRepository.findByUserIdAndTypeAndRecommentId(userId, type, id).isPresent();
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

}
