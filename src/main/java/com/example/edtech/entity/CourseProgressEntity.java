package com.example.edtech.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "course_progress")
@CompoundIndexes({
        @CompoundIndex(name = "user_course_idx",def = "{'userId:1,'courseId':1}",unique = true)
})
@Data
@Builder
public class CourseProgressEntity {
@Id
    private ObjectId id;
private ObjectId courseId;
private ObjectId userId;
private List<LectureEntity>completedLecturesIds;
private LocalDateTime lastUpdated;
}

//2. @CompoundIndexes({...}) and @CompoundIndex(...)
//Purpose:
//        Creates compound indexes in MongoDB.
//        A compound index is an index on multiple fields together, which is useful for speeding up queries that filter or sort on those fields simultaneously.
//
//@CompoundIndexes
//A container annotation that allows you to declare multiple @CompoundIndex definitions in one place.
//
//@CompoundIndex
//java
//        Copy code
//@CompoundIndex(
//        name = "user_course_idx",
//        def = "{'userId': 1, 'courseId': 1}",
//        unique = true
//)
//name → The name of the index in MongoDB (user_course_idx).
//
//        def → The JSON definition of the index fields and their sort order:
//
//        'userId': 1 → ascending order on userId
//
//        'courseId': 1 → ascending order on courseId
//
//        unique = true → Ensures that there cannot be two documents with the same combination of userId and courseId.