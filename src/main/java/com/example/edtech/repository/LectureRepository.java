package com.example.edtech.repository;

import com.example.edtech.entity.LectureEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LectureRepository extends MongoRepository<LectureEntity, ObjectId> {

     List<LectureEntity>findByCourseId(ObjectId courseId);

     void deleteByCourseId(ObjectId objectId);


}
