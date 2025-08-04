package com.example.edtech.entity;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UserEntity {
 @Id
 private String id;
 private String name;
 private String dob;
 private String role;
 private List<String> enrolled;
 private String address;
 private String email;
 private String password;
 private LocalDateTime createdAt;
}
