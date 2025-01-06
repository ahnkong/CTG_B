package com.hoolhool.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoolhool.backend.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByBoardId(Long boardId); // 특정 Board ID로 이미지 조회

    void deleteByBoardId(Long boardId);
}
