package com.hoolhool.backend.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hoolhool.backend.repository.PointTransactionRepository;
import com.hoolhool.backend.repository.UserRepository;
import com.hoolhool.backend.dto.PointTransactionDTO;
import com.hoolhool.backend.dto.UserDTO;
import com.hoolhool.backend.entity.PointTransaction;
import com.hoolhool.backend.entity.PointTransactionChangeType;
import com.hoolhool.backend.entity.User;

import java.time.LocalDateTime;

@Service
public class PointService {
    private final PointTransactionRepository pointTransactionRepository;

    private final UserRepository userRepository;

    public PointService(PointTransactionRepository pointTransactionRepository, UserRepository userRepository) {
        this.pointTransactionRepository = pointTransactionRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public PointTransactionDTO addPoints(String userId, int amount, String description) {
        System.out.println("✅ addPoints 실행됨 - userId: " + userId + ", amount: " + amount);
        
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
        System.out.println("✅ 기존 포인트: " + user.getPoint());
    
        user.setPoint(user.getPoint() + amount);
        userRepository.save(user);
    
        System.out.println("✅ 새로운 포인트: " + user.getPoint());
    
        PointTransaction transaction = new PointTransaction(user, PointTransactionChangeType.EARN, amount, description, LocalDateTime.now());
        transaction = pointTransactionRepository.save(transaction);
    
        System.out.println("✅ 트랜잭션 저장됨 - ID: " + transaction.getTransactionId());
    
        return mapToDTO(transaction);
    }
    
        
    
    @Transactional
    public PointTransactionDTO handleUserRegistration(String userId) {
        // 회원가입 시 기본 100포인트 지급 후 DTO 반환
        return addPoints(userId, 100, "회원가입 축하 포인트");
    }

    private PointTransactionDTO mapToDTO(PointTransaction transaction) {
        return new PointTransactionDTO(
            transaction.getTransactionId(),
            mapToDTO(transaction.getUser()), // ✅ UserDTO 변환 메서드 일관성 유지
            transaction.getChangeType(),
            transaction.getAmount(),
            transaction.getDescription(),
            transaction.getPointTransactionDate()
        );
    }
    

    // User 엔티티 -> UserDTO 변환
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getNickname(),
                user.getName(), // ✅ 추가
                user.getEmail(),
                null, // 비밀번호 제외 (보안)
                user.getInfo(), // ✅ 추가
                user.getMarketing(), // ✅ 추가
                user.getTell(), // ✅ 추가
                user.getProfileImage(),
                user.getPoint(),
                user.getUDate(), // ✅ 추가
                user.getIsActive(), // ✅ 추가
                user.getMbti(), // ✅ 추가
                user.getPersonal(), // ✅ 추가
                user.getSocialType() // ✅ 추가
        );
    }

}
