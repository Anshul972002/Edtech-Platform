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
    private List<ObjectId> likedByUserIds = new ArrayList<>();
   private int replyCount;

    private LocalDateTime createdAt;

    public static CommentEntity toEntity(ObjectId courseId,ObjectId userId
            ,String content,ObjectId parentCommentId,List<ObjectId> newAncestors,
                                         int replyCount
    ){
       return CommentEntity.builder()
                .courseId(courseId)
                .userId(userId)
                .content(content)
                .parentCommentId(parentCommentId)
                .ancestorIds(newAncestors)
                .likedByUserIds(new ArrayList<>())
               .replyCount(replyCount)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
