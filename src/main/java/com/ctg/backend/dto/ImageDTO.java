package com.ctg.backend.dto;

import java.time.LocalDateTime;

import com.ctg.backend.entity.BoardType;
import com.ctg.backend.entity.Image;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private Long imageId;
    private BoardType boardType;
    private Long boardId;
    private String fileName;
    private String filePath;
    private Integer imageOrder;
    private boolean isActive;
    private LocalDateTime uploadedAt;
    private LocalDateTime updatedAt;

    public ImageDTO(Image image) {
        this.imageId = image.getImageId();
        this.boardType = image.getBoardType();
        this.boardId = image.getBoardId();
        this.fileName = image.getFileName();
        this.filePath = image.getFilePath();
        this.imageOrder = image.getImageOrder();
        this.isActive = image.isActive();
        this.uploadedAt = image.getUploadedAt();
        this.updatedAt = image.getUpdatedAt();
    }
    
    public Long getImageId() {
        return imageId;
    }
    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
    public BoardType getBoardType() {
        return boardType;
    }
    public void setBoardType(BoardType boardType) {
        this.boardType = boardType;
    }
    public Long getBoardId() {
        return boardId;
    }
    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public Integer getImageOrder() {
        return imageOrder;
    }
    public void setImageOrder(Integer imageOrder) {
        this.imageOrder = imageOrder;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
