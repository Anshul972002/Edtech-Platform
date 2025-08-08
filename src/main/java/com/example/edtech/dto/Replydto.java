package com.example.edtech.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
public class Replydto {
    private ObjectId id;
    private ObjectId userId;
    private String content;
    private LocalDateTime createdAt;
}
