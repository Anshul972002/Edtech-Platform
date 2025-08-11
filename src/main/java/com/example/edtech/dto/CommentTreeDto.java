package com.example.edtech.dto;

import com.example.edtech.entity.CommentEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CommentTreeDto {
    private String id;
    private String userId;
    private String content;
    private String parentCommentId;
    private int likeCount;
    private LocalDateTime createdAt;
@Builder.Default
    private List<CommentTreeDto> replies=new ArrayList<>(); // for nesting

    public static CommentTreeDto fromEntity(CommentEntity entity) {
        return CommentTreeDto.builder()
                .id(entity.getId().toHexString())
                .parentCommentId(entity.getParentCommentId()!=null?
                        entity.getParentCommentId().toHexString():null)
                .userId(entity.getUserId().toHexString())
                .content(entity.getContent())
                .likeCount(entity.getLikedByUserIds().size())
                .createdAt(entity.getCreatedAt())
                .build();
    }
    }
