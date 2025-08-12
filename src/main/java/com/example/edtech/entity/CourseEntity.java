package com.example.edtech.entity;

import com.example.edtech.dto.Lecturedto;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Builder
@Data
@Document(collection = "courses")
public class CourseEntity {
   @Id
   ObjectId id;
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
private boolean isPublished;
private boolean isPaid;
}
