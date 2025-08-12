package com.example.edtech.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Lecturedto {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Schema(description = "Title of the lecture", example = "Introduction to Java Streams")
    private String title;
   @NotBlank(message = "Lecture no cannot be empty")
    @Schema(description = "Lecture no", example = "1")
    private int lectureNo;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    @Schema(description = "Short description of the lecture content", example = "This lecture covers Java 8 Stream API with examples and best practices.")
    private String description;

    @NotBlank(message = "Video URL is required")
    @Schema(description = "URL of the lecture video (usually a CDN or cloud storage link)", example = "https://cdn.example.com/videos/java-streams.mp4")
    private String videoUrl;

    @Min(value = 1, message = "Duration should be at least 1 minute")
    @Max(value = 300, message = "Duration cannot exceed 300 minutes")
    @Schema(description = "Duration of the lecture in minutes", example = "60")
    private int durationInMinutes;
}

