package com.example.edtech.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private ObjectId id;

    private ObjectId courseId;                // Course this comment belongs to
    private ObjectId userId;                  // Author
    private String content;                 // Comment text

    private ObjectId parentCommentId;       // null for top-level
    private List<ObjectId> ancestorIds;     // For easier thread queries

    @Builder.Default
    private List<String> likedByUserIds = new ArrayList<>();

    private LocalDateTime createdAt;
}
