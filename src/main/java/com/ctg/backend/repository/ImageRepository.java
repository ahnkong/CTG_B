package com.ctg.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ctg.backend.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByBoard_BoardId(Long boardId); // 특정 Board ID로 이미지 조회

    void deleteByBoard_BoardId(Long boardId);
}
