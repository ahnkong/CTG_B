package com.hoolhool.backend.repository;

import com.hoolhool.backend.entity.User;
import com.hoolhool.backend.entity.Quest;
import com.hoolhool.backend.entity.UserQuestTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQuestTransactionRepository extends JpaRepository<UserQuestTransaction, Long> {
    
    // ✅ 유저 객체와 퀘스트 객체로 조회
    Optional<UserQuestTransaction> findByUserAndQuest(User user, Quest quest);

    // ✅ 유저 ID와 퀘스트 ID로 조회 (ID 값 기준)
    Optional<UserQuestTransaction> findByUser_UserIdAndQuest_QuestId(String userId, Long questId);
}
