package com.example.edtech.repository;

import com.example.edtech.entity.CourseEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<CourseEntity, ObjectId> {
    boolean existsByTitle(String title);

}
