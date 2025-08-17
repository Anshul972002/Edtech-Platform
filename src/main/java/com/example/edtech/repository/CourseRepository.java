package com.example.edtech.repository;

import com.example.edtech.entity.CourseEntity;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository extends MongoRepository<CourseEntity, ObjectId> {
    boolean existsByTitle(String title);

    List<CourseEntity> findByIdIn(List<ObjectId> courseIds);

    Page<CourseEntity> findByIsPublishedTrue(Pageable pageable);

   Page<CourseEntity> findByCreatedBy(String teacherId, PageRequest pageRequest);

    Page<CourseEntity> findByCreatedByAndIsPaidFalse(String teacherId, PageRequest pageRequest);
    Page<CourseEntity> findByCreatedByAndIsPaidTrue(String teacherId, PageRequest pageRequest);

    List<CourseEntity> findByEnrolledUserContains(ObjectId userId);
}
