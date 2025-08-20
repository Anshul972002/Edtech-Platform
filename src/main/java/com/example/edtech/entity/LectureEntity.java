package com.example.edtech.entity;

import com.example.edtech.dto.Lecturedto;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
@Document(collection = "lectures")
public class LectureEntity {

    @Id
  private ObjectId id;
    private String title;
    private  int lectureNo;
private String description;
private Map<String,String>videoUrl;
private int durationInMinutes;
private LocalDateTime createdAt;
private  ObjectId courseId;

public  static LectureEntity toEntity(Lecturedto lecture){
    return LectureEntity.builder()
            .title(lecture.getTitle())
            .description(lecture.getDescription())
            .lectureNo(lecture.getLectureNo())
            .durationInMinutes(lecture.getDurationInMinutes())
            .createdAt(LocalDateTime.now())
            .build();
}
}
