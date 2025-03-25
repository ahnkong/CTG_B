package com.hoolhool.backend.repository;


// import java.util.List;
// import org.springframework.data.jpa.repository.JpaRepository;
// // -추가 3.17
// public class PointTransactionRepository {
//     public interface PointTransaction extends JpaRepository<PointTransactionRepository, Long> {
//         List<PointTransactionRepository> findByUserId(Long userId);
//     }
// }

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoolhool.backend.entity.PointTransaction;

import java.util.List;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByUserUserId(String userId); // 🔥 수정 필수!
}