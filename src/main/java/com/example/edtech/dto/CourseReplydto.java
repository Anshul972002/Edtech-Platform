package com.example.edtech.dto;

import com.example.edtech.entity.CourseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Data
public class CourseReplydto {


   private String id;
    private String title;

    private String description;

    private String category;
    private String thumbnailUrl;
    private boolean isPublished;
    private List<ObjectId>lectureId;
private boolean isPaid;
    public static CourseReplydto fromEntity(CourseEntity course){
        return CourseReplydto.builder().title(course.getTitle()).description(course.getDescription())
                .category(course.getCategory())
                .id(course.getId().toHexString())
                .lectureId(course.getLectureId())
                .thumbnailUrl(course.getThumbnailUrl())
                .isPublished(course.isPublished())
                .build();
    }
}
