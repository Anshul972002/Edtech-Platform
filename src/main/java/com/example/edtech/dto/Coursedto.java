package com.example.edtech.dto;

import com.example.edtech.entity.CourseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class Coursedto {


    @NotBlank(message = "Enter the title")
    @Schema(example = "Java Essentials", description = "Title of the course")
    private String title;

    @NotBlank(message = "Enter the description")
    @Schema(example = "Learn Java from scratch", description = "Brief overview of the course")
    private String description;

    @NotBlank(message = "Enter the category")
    @Schema(example = "Development", description = "Category of the course")
    private String category;

    @NotBlank(message = "Enter the URL")
    @Schema(example = "https://example.com/image.jpg", description = "Thumbnail image URL")
    private String thumbnailUrl;

    @Schema(example = "false", description = "Is the course published?")
    private boolean isPublished;


    public static Coursedto fromEntity(CourseEntity course){
         return Coursedto.builder().title(course.getTitle()).description(course.getDescription())
                 .category(course.getCategory())

                 .thumbnailUrl(course.getThumbnailUrl())
                 .isPublished(course.isPublished())
                 .build();
    }
}