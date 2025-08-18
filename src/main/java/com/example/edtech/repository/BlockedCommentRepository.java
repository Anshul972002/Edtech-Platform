package com.example.edtech.repository;

import com.example.edtech.entity.BlockedCommentEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlockedCommentRepository extends MongoRepository<BlockedCommentEntity, ObjectId> {
    boolean existsByUserId(ObjectId userId);
}
