package com.example.edtech.repository;

import com.example.edtech.dto.Userdto;
import com.example.edtech.entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
    UserEntity findByEmail(String email);

    List<UserEntity> findByEnrolledCoursesContains(ObjectId id);





}
//    And you call:
//
//        userRepository.findByEnrolledCoursesContains(ObjectId("64f8c..."));
//
//
//        Spring Data MongoDB translates it into:
//
//        {
//        "enrolledCourses": { "$in": [ ObjectId("64f8c...") ] }
//        }
//
