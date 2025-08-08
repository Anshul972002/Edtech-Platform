package com.example.edtech.repository;

import com.example.edtech.entity.CommentEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<CommentEntity, ObjectId> {

}
