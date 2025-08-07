package com.example.edtech.entity;

import com.example.edtech.dto.Lecturedto;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@Document(collection = "courses")
public class CourseEntity {
   @Id
   String id;

   String title;
String description;
String category;
String thumbnailUrl;
ObjectId createdBy;
LocalDateTime createdAt;
LocalDateTime updatedAt;

List<ObjectId> lectureId;
//List<Quiz>quizzes;
List<ObjectId>enrolledUser;
boolean isPublished;
}
