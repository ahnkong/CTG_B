package com.hoolhool.backend.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image {
    @Id
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "image_order", columnDefinition = "INT")
    private Integer imageOrder;
}
