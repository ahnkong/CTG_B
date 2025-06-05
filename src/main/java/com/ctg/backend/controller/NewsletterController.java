package com.ctg.backend.controller;

import com.ctg.backend.dto.NewsletterDTO;
import com.ctg.backend.entity.Newsletter;
import com.ctg.backend.entity.User;
import com.ctg.backend.service.NewsletterService;
import com.ctg.backend.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/bulletins")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;
    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createNewsletter(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String bulletinDate,
            @RequestParam("file") MultipartFile file) {
        try {
            User user = userService.findByIdEntity(userId);
            newsletterService.create(title, content, bulletinDate, file, user);
            return ResponseEntity.ok("업로드 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("업로드 실패: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<NewsletterDTO>> getAllNewsletters() {
        List<NewsletterDTO> newsletterList = newsletterService.getAllAsDTO(Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(newsletterList);
    }

}
