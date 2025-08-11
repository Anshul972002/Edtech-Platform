package com.example.edtech.entity;

import com.example.edtech.dto.Lecturedto;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Data
@Document(collection = "lectures")
public class LectureEntity {

    @Id
    ObjectId id;
    private String title;
private String description;
private String videoUrl;
private int durationInMinutes;
private LocalDateTime createdAt;

public  static LectureEntity toEntity(Lecturedto lecture){
    return LectureEntity.builder()
            .title(lecture.getTitle())
            .description(lecture.getDescription())
            .durationInMinutes(lecture.getDurationInMinutes())
            .videoUrl(lecture.getVideoUrl())
            .createdAt(LocalDateTime.now())
            .build();
}
}
