package com.example.edtech.repository;

import com.example.edtech.entity.CourseProgressEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseProgressRepository extends MongoRepository<CourseProgressEntity, ObjectId> {

    Optional<CourseProgressEntity> findByUserIdAndCourseId(ObjectId userId, ObjectId courseId);
}
