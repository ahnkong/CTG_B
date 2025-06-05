package com.ctg.backend.repository;

import com.ctg.backend.entity.BookUnified;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookUnifiedRepository extends JpaRepository<BookUnified, Long> {
    // 기존 단일 조회
    Optional<BookUnified> findByChapterNumberAndSectionNumber(Integer chapterNumber, Integer sectionNumber);

    // 추가: 특정 챕터의 모든 절(Section)을 번호 순으로 가져오기
    List<BookUnified> findAllByChapterNumberOrderBySectionNumber(Integer chapterNumber);
}
