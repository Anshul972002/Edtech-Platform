package com.example.edtech.dto;

import com.example.edtech.entity.CommentEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentTreeDto {
    private String id;
    private String userId;
    private String content;
    private int likeCount;
    private LocalDateTime createdAt;
    private List<CommentTreeDto> replies; // for nesting

    public static CommentTreeDto fromEntity(CommentEntity entity) {
        return CommentTreeDto.builder()
                .id(entity.getId().toHexString())

                .userId(entity.getUserId().toHexString())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();


    }
    }
