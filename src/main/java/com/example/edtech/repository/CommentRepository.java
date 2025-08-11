package com.example.edtech.repository;

import com.example.edtech.entity.CommentEntity;
import org.apache.el.stream.Stream;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<CommentEntity, ObjectId> {

    List<CommentEntity> findByCourseId(ObjectId courseId);


 Page<CommentEntity> findByCourseIdAndParentCommentId(ObjectId courseId, ObjectId parentId, PageRequest of);


    long countByParentCommentId(ObjectId parentId);
}
