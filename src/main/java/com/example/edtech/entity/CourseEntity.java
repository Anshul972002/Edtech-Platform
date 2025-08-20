package com.example.edtech.entity;

import com.example.edtech.dto.Lecturedto;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Builder
@Data
@Document(collection = "courses")
public class CourseEntity {
   @Id
   ObjectId id;
   String title;
String description;
String category;
ObjectId createdBy;
LocalDateTime createdAt;
LocalDateTime updatedAt;

Map<String,String> thumbnail;
List<ObjectId> lectureId;
//List<Quiz>quizzes;
List<ObjectId>enrolledUser;
private boolean isPublished;
private boolean isPaid;

}
