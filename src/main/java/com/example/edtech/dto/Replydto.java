package com.example.edtech.dto;

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
    private List<String> likedByUserIds;
    private LocalDateTime createdAt;
}
