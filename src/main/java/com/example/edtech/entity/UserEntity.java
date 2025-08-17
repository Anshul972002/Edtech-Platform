package com.example.edtech.entity;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UserEntity {
 @Id
 private ObjectId id;
 private String name;
 private String dob;
 private String role;
 private List<ObjectId> enrolledCourses;
 private String address;
 private String email;
 private String password;
 private LocalDateTime createdAt;
 private Map<String,String > profile;
 private boolean accountLocked=false;
 private boolean enabled=true;
}
