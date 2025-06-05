package com.ctg.backend.repository;

import com.ctg.backend.entity.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    // 필요 시 커스텀 쿼리 작성 가능
}
