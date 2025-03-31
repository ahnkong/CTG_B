package com.ctg.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctg.backend.dto.QuestDTO;
import com.ctg.backend.entity.User;
import com.ctg.backend.repository.UserRepository;
import com.ctg.backend.service.QuestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/v1/quest")
public class QuestController {
    
    private final QuestService questService;
    private final UserRepository userRepository; // ✅ 여기에 추가!


    // ✅ 퀘스트 목록 조회 (유저 ID 포함)
    @GetMapping("/list")
    public ResponseEntity<List<QuestDTO>> getAllQuests(@RequestParam String userId) { // ✅ userId 추가
        List<QuestDTO> quests = questService.getAllQuests(userId);
        return ResponseEntity.ok(quests);
    }

    // ✅ 퀘스트 진행 API
    @PostMapping("/progress")
    public ResponseEntity<String> progressQuest(@RequestParam String userId, @RequestParam Long questId) {
        questService.progressQuest(userId, questId);
        return ResponseEntity.ok("퀘스트 진행 완료!");
    }


    // ✅ 퀘스트 - 마케팅 동의여부 API
    @PostMapping("/progress/marketing")
    public ResponseEntity<String> progressMarketingQuest(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
    
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저 없음"));
    
        if (Boolean.TRUE.equals(user.getMarketing())) {
            return ResponseEntity.badRequest().body("이미 마케팅 동의함");
        }
    
        // 마케팅 동의 처리
        user.setMarketing(true);
        userRepository.save(user);
    
        // 퀘스트 처리 (예: 마케팅 동의 퀘스트 ID = 2L)
        questService.progressQuest(userId, 2L);
    
        return ResponseEntity.ok("마케팅 동의 + 포인트 지급 완료!");
    }
    

}