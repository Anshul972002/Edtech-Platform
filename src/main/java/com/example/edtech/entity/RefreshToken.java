package com.example.edtech.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@Builder
public class RefreshToken {
private ObjectId id;
private ObjectId userId;
private String token;
private Instant expiryDate;
}
