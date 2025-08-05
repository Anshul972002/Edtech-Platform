package com.example.edtech.repository;

import com.example.edtech.entity.LectureEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LectureRepository extends MongoRepository<LectureEntity, ObjectId> {

}
