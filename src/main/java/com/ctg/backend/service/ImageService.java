package com.ctg.backend.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ctg.backend.dto.ImageDTO;
import com.ctg.backend.entity.Board;
import com.ctg.backend.entity.Image;
import com.ctg.backend.repository.BoardRepository;
import com.ctg.backend.repository.ImageRepository;

@Service
public class ImageService {
    
    @Value("${file.upload-dir}") // 업로드 경로
    private String uploadDir;

    private final ImageRepository imageRepository;
    private final BoardRepository boardRepository;

    public ImageService(ImageRepository imageRepository, BoardRepository boardRepository) {
        this.imageRepository = imageRepository;
        this.boardRepository = boardRepository;
    }

    public List<ImageDTO> saveImages(List<MultipartFile> files, Long boardId) throws IOException {
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            boolean dirCreated = uploadPath.mkdirs(); // 디렉토리 생성
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));

        List<Image> images = new ArrayList<>();

        // 현재 날짜를 "yyyyMMdd" 형식으로 변환
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        for (MultipartFile file : files) {
            // 파일 이름을 "날짜-원본파일이름" 형식으로 변경
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = currentDate + "-" + originalFileName;
            
            String filePath = uploadDir + "/" + uniqueFileName;
            String relativeFilePath = "uploads/" + uniqueFileName;

            File dest = new File(filePath);
            file.transferTo(dest);

            // 이미지 엔티티 생성
            Image image = new Image();
            image.setBoard(board);
            image.setFilePath(relativeFilePath);
            image.setFileName(uniqueFileName);
            images.add(image);
        }

        List<Image> savedImages = imageRepository.saveAll(images);

        // 엔티티를 DTO로 변환
        List<ImageDTO> imageDTOs = new ArrayList<>();
        for (Image image : savedImages) {
            imageDTOs.add(convertToDTO(image));
        }

        return imageDTOs;
    }

    public List<ImageDTO> getImagesByBoardId(Long boardId) {
        List<Image> images = imageRepository.findByBoard_BoardId(boardId);

        List<ImageDTO> imageDTOs = new ArrayList<>();
        for (Image image : images) {
            imageDTOs.add(convertToDTO(image));
        }

        return imageDTOs;
    }

    // 게시글에 연결된 모든 이미지 삭제
    public void deleteImagesByBoardId(Long boardId) {
        imageRepository.deleteByBoard_BoardId(boardId);
    }

    // 🔹 엔티티 -> DTO 변환 (boardId만 포함)
    private ImageDTO convertToDTO(Image image) {
        return new ImageDTO(
                image.getImageId(),
                image.getBoard().getBoardId(), // Board 객체에서 boardId 추출
                image.getFileName(),
                image.getFilePath(),
                image.getImageOrder()
        );
    }
}
