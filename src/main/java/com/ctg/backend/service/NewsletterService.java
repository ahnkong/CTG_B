package com.ctg.backend.service;

import com.ctg.backend.dto.ImageDTO;
import com.ctg.backend.dto.NewsletterDTO;
import com.ctg.backend.entity.*;
import com.ctg.backend.repository.DomainRepository;
import com.ctg.backend.repository.ImageRepository;
import com.ctg.backend.repository.NewsletterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsletterService {

    private final NewsletterRepository newsletterRepository;
    private final ImageRepository imageRepository;
    private final DomainRepository domainRepository;

    public void create(String title, String content, String bulletinDate, MultipartFile file, User user)
            throws IOException {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        Domain domain = user.getDomain();
        if (domain == null) {
            domain = domainRepository.findByDomainName("숭신교회")
                    .orElseThrow(() -> new IllegalStateException("'숭신교회' 도메인이 존재하지 않습니다."));
        }

        Newsletter newsletter = new Newsletter();
        newsletter.setTitle(title);
        newsletter.setContent(content);
        newsletter.setUser(user);
        newsletter.setDomain(domain);

        Newsletter savedNewsletter = newsletterRepository.save(newsletter);

        if (file != null && !file.isEmpty()) {
            String savedPath = saveFile(file);
            String originalName = file.getOriginalFilename();

            Image image = new Image();
            image.setNewsletter(savedNewsletter);
            image.setBoardType(BoardType.NEWSLETTER);
            image.setBoardId(savedNewsletter.getBoardId());
            image.setFileName(originalName);
            image.setFilePath(savedPath);
            image.setActive(true);

            imageRepository.save(image);
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String baseDir = System.getProperty("user.dir");
        String uploadDir = baseDir + File.separator + "uploads" + File.separator + "newsletter";

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists() && !uploadPath.mkdirs()) {
            throw new IOException("업로드 디렉토리 생성 실패: " + uploadDir);
        }

        String originalFilename = file.getOriginalFilename();
        String uniqueName = UUID.randomUUID() + "_" + originalFilename;
        Path fullPath = Paths.get(uploadDir, uniqueName);
        file.transferTo(fullPath.toFile());

        return "/uploads/newsletter/" + uniqueName;
    }

    public List<Newsletter> getAll(Sort sort) {
        return newsletterRepository.findAll(sort);
    }

    public List<NewsletterDTO> getAllAsDTO(Sort sort) {
        List<Newsletter> newsletters = newsletterRepository.findAll(sort);

        return newsletters.stream().map(newsletter -> {
            NewsletterDTO dto = new NewsletterDTO(newsletter);

            // 유저 정보 추가
            dto.setUserNickname(newsletter.getUser().getNickname());
            dto.setUserProfileImage(newsletter.getUser().getProfileImage());

            // 이미지 리스트 매핑
            List<ImageDTO> imageDTOs = newsletter.getImages().stream()
                    .filter(Image::isActive) // 비활성 이미지는 제외할 경우
                    .map(ImageDTO::new)
                    .collect(Collectors.toList());

            dto.setImages(imageDTOs);

            return dto;
        }).collect(Collectors.toList());
    }

}
