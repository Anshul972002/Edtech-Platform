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
    private ObjectId id;
    private ObjectId userId;
    private String content;
    private LocalDateTime createdAt;
    private List<Replydto> replies;
    }


