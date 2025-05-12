package com.ctg.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ctg.backend.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);

    // 핸드폰 번호 중복 확인
    boolean existsByTell(String tell);

    // 이메일 찾기
    Optional<User> findByNameAndTell(String name, String tell);

    // 비밀번호 변경을 위한 사용자 검증
    Optional<User> findByNameAndEmail(String name, String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.profileImage = :profileImage WHERE u.userId = :userId")
    void updateProfileImage(@Param("userId") Long userId, @Param("profileImage") String profileImage);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.info = :info WHERE u.userId = :userId")
    void updateUserInfo(@Param("userId") Long userId, @Param("info") String info);

    //마케팅 동의 여부 변경
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.agreeToMarketing = :agreeToMarketing WHERE u.userId = :userId")
    void updateMarketing(@Param("userId") Long userId, @Param("agreeToMarketing") Boolean agreeToMarketing);

    // 기본 조회 메서드
    Optional<User> findByEmail(String email);
}
