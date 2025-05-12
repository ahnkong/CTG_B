package com.ctg.backend.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ctg.backend.dto.ImageDTO;
import com.ctg.backend.entity.Image;
import com.ctg.backend.repository.ImageRepository;
import com.ctg.backend.entity.Community;
import com.ctg.backend.entity.Notice;
import com.ctg.backend.entity.Newsletter;
import com.ctg.backend.entity.BoardType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ImageService {
    
    @Value("${file.upload-dir}") // 업로드 경로
    private String uploadDir;

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public List<ImageDTO> saveImages(List<MultipartFile> files, Object board, BoardType boardType) throws IOException {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        List<Image> images = new ArrayList<>();
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = currentDate + "-" + originalFileName;
            
            String filePath = uploadDir + "/" + uniqueFileName;
            String relativeFilePath = "uploads/" + uniqueFileName;

            File dest = new File(filePath);
            file.transferTo(dest);

            Image image = new Image();
            image.setBoardType(boardType);
            
            // 게시판 타입에 따라 적절한 엔티티 설정
            switch (boardType) {
                case COMMUNITY:
                    image.setCommunity((Community) board);
                    image.setBoardId(((Community) board).getBoardId());
                    break;
                case NOTICE:
                    image.setNotice((Notice) board);
                    image.setBoardId(((Notice) board).getBoardId());
                    break;
                case NEWSLETTER:
                    image.setNewsletter((Newsletter) board);
                    image.setBoardId(((Newsletter) board).getBoardId());
                    break;
                default:
                    throw new IllegalArgumentException("지원하지 않는 게시판 타입입니다: " + boardType);
            }

            image.setFileName(uniqueFileName);
            image.setFilePath(relativeFilePath);
            image.setImageOrder(images.size());
            image.setActive(true);
            
            images.add(image);
        }

        List<Image> savedImages = imageRepository.saveAll(images);
        return savedImages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        }

    @Transactional
    public void deleteImages(Object board, BoardType boardType) {
        List<Image> images;
        
        // 게시판 타입에 따라 적절한 이미지 조회
        switch (boardType) {
            case COMMUNITY:
                images = imageRepository.findByCommunity((Community) board);
                break;
            case NOTICE:
                images = imageRepository.findByNotice((Notice) board);
                break;
            case NEWSLETTER:
                images = imageRepository.findByNewsletter((Newsletter) board);
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 게시판 타입입니다: " + boardType);
        }
        
        // 실제 파일 삭제
        for (Image image : images) {
            File file = new File(uploadDir + "/" + image.getFileName());
            if (file.exists()) {
                file.delete();
            }
        }
        
        imageRepository.deleteAll(images);
    }

    private ImageDTO convertToDTO(Image image) {
        return new ImageDTO(
                image.getImageId(),
            image.getBoardType(),
            image.getBoardId(),
                image.getFileName(),
                image.getFilePath(),
            image.getImageOrder(),
            image.isActive(),
            image.getUploadedAt(),
            image.getUpdatedAt()
        );
    }
}
