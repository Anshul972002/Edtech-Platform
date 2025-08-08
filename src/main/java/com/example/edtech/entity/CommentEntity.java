package com.example.edtech.entity;

import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private String id;

    private String courseId;      // The course this comment belongs to
    private String userId;        // Who posted it
    private String content;
    private List<ObjectId> parentCommentId;  // null if it's a top-level comment, otherwise a reply
    private List<ObjectId> likedByUserIds; // Optional
    private LocalDateTime createdAt;
}
