package com.example.edtech.service;

import com.example.edtech.dto.Commentdto;
import com.example.edtech.dto.Replydto;
import com.example.edtech.entity.CommentEntity;
import com.example.edtech.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private Commentdto mapToDto(CommentEntity entity){
        return Commentdto.builder()
                .id(entity.getId().toHexString())
                .courseId(entity.getCourseId().toHexString())
                .userId(entity.getUserId().toHexString())
                .content(entity.getContent())
                .likedByUserIds(entity.getLikedByUserIds())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private Replydto mapToReplyDto(CommentEntity entity){
        return Replydto.builder()
                .id(entity.getId().toHexString())
                .userId(entity.getUserId().toHexString())
                .content(entity.getContent())
                .likedByUserIds(entity.getLikedByUserIds())
                .createdAt(entity.getCreatedAt())
                .build();
    }
    public Commentdto addComment(ObjectId courseId,ObjectId userId,String content){
        CommentEntity comment=CommentEntity.builder()
                .courseId(courseId)
                .userId(userId)
                .content(content)
                .parentCommentId(null)          // top-level comment
                .ancestorIds(new ArrayList<>()) // no parents
                .likedByUserIds(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();
        CommentEntity save = commentRepository.save(comment);
        return mapToDto(save);
    }


    public Replydto addReply(ObjectId courseId, ObjectId parentCommentId, ObjectId userId, String content) {
        //  Find the parent comment
        CommentEntity parent = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        //  Build ancestor list: copy parent's ancestors + parent ID
        List<ObjectId> newAncestors = new ArrayList<>(parent.getAncestorIds());
        newAncestors.add(parent.getId());

        //  Create reply comment
        CommentEntity comment = CommentEntity.builder()
                .courseId(courseId)
                .userId(userId)
                .content(content)
                .parentCommentId(parentCommentId)
                .ancestorIds(newAncestors)
                .likedByUserIds(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        CommentEntity saved = commentRepository.save(comment);
        return mapToReplyDto(saved);
    }

    public Object getCommentsTree(String courseId) {

    }
}
