package com.ctg.backend.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private Long imageId;
    private Long boardId;
    private String fileName;
    private String filePath;
    private Integer imageOrder;
    
    public Long getImageId() {
        return imageId;
    }
    public void setImageId(Long imageId) {
        this.imageId = imageId;
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


    



}
