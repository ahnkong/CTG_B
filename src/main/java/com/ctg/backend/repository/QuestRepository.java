package com.ctg.backend.repository;

// -추가 3.17
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ctg.backend.entity.Quest;

@Repository // ✅ 반드시 추가!
public interface QuestRepository extends JpaRepository<Quest, Long> {
}