package com.example.edtech.dto;

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

        String id;
     @NotBlank(message = "Enter the title")
        String title;
      @NotBlank(message = "Enter the description ")
        String description;
        @NotBlank(message = "Enter the category ")
        String category;
        @NotBlank(message = "Enter the url ")
        String thumbnailUrl;
      @NotBlank(message = "Want to publish")
        boolean isPublished;

    }

