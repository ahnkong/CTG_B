
package com.ctg.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctg.backend.dto.QuestDTO;
import com.ctg.backend.entity.PointTransaction;
import com.ctg.backend.entity.PointTransactionChangeType;
import com.ctg.backend.entity.Quest;
import com.ctg.backend.entity.User;
import com.ctg.backend.entity.UserQuestTransaction;
import com.ctg.backend.entity.UserQuestTransactionStatus;
import com.ctg.backend.repository.BoardRepository;
import com.ctg.backend.repository.PointTransactionRepository;
import com.ctg.backend.repository.QuestRepository;
import com.ctg.backend.repository.UserQuestTransactionRepository;
import com.ctg.backend.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuestService {
    private final BoardRepository boardRepository;
    private final QuestRepository questRepository;
    private final PointService pointService;
    private final UserRepository userRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final UserQuestTransactionRepository userQuestTransactionRepository;

    public QuestService(BoardRepository boardRepository,
            QuestRepository questRepository,
            PointService pointService,
            UserRepository userRepository,
            PointTransactionRepository pointTransactionRepository,
            UserQuestTransactionRepository userQuestTransactionRepository) {
        this.boardRepository = boardRepository;
        this.questRepository = questRepository;
        this.pointService = pointService;
        this.userRepository = userRepository;
        this.pointTransactionRepository = pointTransactionRepository;
        this.userQuestTransactionRepository = userQuestTransactionRepository;
    }

    // ✅ 회원가입 시 기본 포인트 지급
    @Transactional
    public void handleUserRegistration(String userId) {
        pointService.addPoints(userId, 100, "회원가입 축하 포인트");
    }

    // 퀘스트 모든 리스트 불러오기
    // ✅ 퀘스트 전체 목록 불러오기 (진행도 포함)
    @Transactional(readOnly = true)
    public List<QuestDTO> getAllQuests(String userId) { // 유저 ID를 받도록 수정
        List<Quest> quests = questRepository.findAll();

        return quests.stream().map(quest -> {
            // ✅ 유저의 퀘스트 진행도 조회 (없으면 0)
            UserQuestTransaction userQuest = userQuestTransactionRepository
            .findByUser_UserIdAndQuest_QuestId(userId, quest.getQuestId())
            .orElse(null); // 없으면 null 처리
    
    int progress = (userQuest != null) ? userQuest.getProgress() : 0;

            return new QuestDTO(
                    quest.getQuestId(),
                    quest.getQName(),
                    quest.getQDescription(),
                    quest.getRewardPoint(),
                    quest.getIsActive(),
                    quest.getQuestDate(),
                    quest.getRequiredAttempts(),
                    progress // ✅ 진행도 값 추가!
            );
        }).collect(Collectors.toList());
    }

    // ✅ 퀘스트 진행 로직
    @Transactional
    public void progressQuest(String userId, Long questId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new RuntimeException("퀘스트 없음"));

        UserQuestTransaction userQuest = userQuestTransactionRepository
                .findByUserAndQuest(user, quest)
                .orElseGet(() -> {
                    UserQuestTransaction newQuest = new UserQuestTransaction();
                    newQuest.setUser(user);
                    newQuest.setQuest(quest);
                    newQuest.setProgress(0);
                    newQuest.setStatus(UserQuestTransactionStatus.IN_PROGRESS);
                    newQuest.setUQDate(LocalDateTime.now());
                    return newQuest;
                });

        // ✅ ✅ ✅ 요기! 로그 추가
        // ✅ 여기에 넣어줘!!!
        if (userQuest.getStatus() == UserQuestTransactionStatus.COMPLETED) {
            log.info("이미 완료된 퀘스트이므로 포인트는 지급되지 않습니다.");
            return; // ❌ 예외 던지지 말고 그냥 종료
        }

        userQuest.setProgress(userQuest.getProgress() + 1);

        if (userQuest.getProgress() >= quest.getRequiredAttempts()) {
            userQuest.setStatus(UserQuestTransactionStatus.COMPLETED);
            user.setPoint(user.getPoint() + quest.getRewardPoint());

            PointTransaction pt = new PointTransaction();
            pt.setUser(user);
            pt.setAmount(quest.getRewardPoint());
            pt.setChangeType(PointTransactionChangeType.EARN);
            pt.setDescription("퀘스트 완료 보상");
            pt.setPointTransactionDate(LocalDateTime.now());
            pointTransactionRepository.save(pt);
        }

        userQuestTransactionRepository.save(userQuest);
        userRepository.save(user);
    }

}
