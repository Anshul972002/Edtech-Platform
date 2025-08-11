package com.example.edtech.dto;

import com.example.edtech.entity.CommentEntity;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Replydto {
    private String id;
    private String userId;
    private String content;
    private int  likeCount;
    private int replyCount;
    private LocalDateTime createdAt;

    public static Replydto fromEntity(CommentEntity entity){
        return Replydto.builder()
                .id(entity.getId().toHexString())
                .userId(entity.getUserId().toHexString())
                .content(entity.getContent())
                .likeCount(entity.getLikedByUserIds().size())
                .replyCount(entity.getReplyCount())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
