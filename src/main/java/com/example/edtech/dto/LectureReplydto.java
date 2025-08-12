package com.example.edtech.dto;

import com.example.edtech.entity.LectureEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LectureReplydto {


 private String id;

 private String title;
private String courseId;

private String description;


private String videoUrl;

private int durationInMinutes;

    public  static LectureReplydto fromEntity(LectureEntity lecture){
        return LectureReplydto.builder()
                .id(lecture.getId().toHexString())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .courseId(lecture.getCourseID().toHexString())
                .durationInMinutes(lecture.getDurationInMinutes())
                .videoUrl(lecture.getVideoUrl())
                .build();
    }
    }

