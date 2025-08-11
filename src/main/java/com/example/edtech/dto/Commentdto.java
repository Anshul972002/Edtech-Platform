package com.example.edtech.dto;

import com.example.edtech.entity.CommentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Commentdto {
    private String id;
    private String courseId;
    private String userId;
    private String content;
    private int likeCount;
    private int replyCount;
    private LocalDateTime createdAt;


    public static Commentdto fromEntity(CommentEntity entity) {
        return Commentdto.builder()
                .id(entity.getId().toHexString())
                .courseId(entity.getCourseId().toHexString())
                .userId(entity.getUserId().toHexString())
                .content(entity.getContent())
                .likeCount(entity.getLikedByUserIds().size())
                .replyCount(entity.getReplyCount())
                .createdAt(entity.getCreatedAt())
                .build();

    }
}



