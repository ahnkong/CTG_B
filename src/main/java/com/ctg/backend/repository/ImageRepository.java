package com.ctg.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ctg.backend.entity.Community;
import com.ctg.backend.entity.Notice;
import com.ctg.backend.entity.Newsletter;
import com.ctg.backend.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByCommunity(Community community);
    List<Image> findByNotice(Notice notice);
    List<Image> findByNewsletter(Newsletter newsletter);
}
