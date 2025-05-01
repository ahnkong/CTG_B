package com.ctg.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MyCommentDTO {
    private Long commentId;
    private Long boardId;
    private String content;
    private LocalDateTime createDate;
}