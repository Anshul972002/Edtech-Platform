package com.example.edtech.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "lectures")
public class LectureEntity {
private String title;
private String description;
private String videoUrl;
private int durationInMinutes;
private LocalDateTime createdAt;
}
