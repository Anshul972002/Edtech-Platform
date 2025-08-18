package com.example.edtech.entity;

import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Builder
@Document(collection = "blocked_comments")
public class BlockedCommentEntity {
    @Id
    private ObjectId id;
    private ObjectId courseId;
    private ObjectId userId;
    private LocalDateTime blockedAt;
}
