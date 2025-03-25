package com.hoolhool.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hoolhool.backend.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    
    // ID 중복 확인
    boolean existsById(String userId);

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);

    // 핸드폰 번호 중복 확인
    boolean existsByTell(String tell);

    // 사용자 검색 메서드
    Optional<User> findByNameAndTellAndEmail(String name, String tell, String email);
    Optional<User> findByUserIdAndNameAndEmail(String userId, String name, String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.profileImage = :profileImage WHERE u.userId = :userId")
    void updateProfileImage(@Param("userId") String userId, @Param("profileImage") String profileImage);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.info = :info WHERE u.userId = :userId")
    void updateUserInfo(@Param("userId") String userId, @Param("info") String info);


    //마케팅 동의 여부 변경
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.marketing = :marketing WHERE u.userId = :userId")
    void updateMarketing(@Param("userId") String userId, @Param("marketing") Boolean marketing);


    //  ID가 있는지 확인
    Optional<User> findByUserId(String userId);




}
