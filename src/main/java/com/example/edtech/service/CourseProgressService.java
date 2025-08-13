package com.example.edtech.service;

import com.example.edtech.dto.CourseProgressdto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.CourseProgressEntity;
import com.example.edtech.repository.CourseProgressRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


// Impact on Spring dependency injection
//         With @RequiredArgsConstructor, only final and @NonNull fields get constructor parameters — perfect for dependency injection because it ignores non-essential fields.
//
//        With @AllArgsConstructor, all fields become constructor parameters.
//
//        This means if you add a field for some reason (e.g., a config string, a test flag) and don’t want Spring to inject it, Spring will still try to resolve a bean for it — and fail if it can’t find one.
//
//        This can cause NoSuchBeanDefinitionException if Spring can’t find a matching bean.
//@RequiredArgsConstructor by default only includes:
//
//final fields
//
//Fields annotated with @NonNull
//
//So, if you want constructor injection for a non-final dependency but still want it included in the generated constructor, you can mark it with @NonNull.
@Service
@RequiredArgsConstructor
public class CourseProgressService {

    private final CourseProgressRepository progressRepository;

    public double getProgressPercentage(CourseEntity course, CourseProgressEntity progress) {
        int totalLectures = course.getLectureId().size();
        int completedLectures = progress.getCompletedLectureIds().size();

        if (totalLectures == 0) return 0;
        return (completedLectures * 100.0) / totalLectures;
    }

    public CourseProgressEntity getProgress(ObjectId userId,ObjectId courseId){
return          progressRepository.findByUserIdAndCourseId(userId, courseId).orElse(
                 CourseProgressEntity.builder().userId(userId).courseId(courseId)
                         .completedLectureIds(new ArrayList<>()).build()
         );

    }

    public CourseProgressdto markLectureAsCompleted(ObjectId userId, ObjectId courseId, ObjectId lectureId) {
        CourseProgressEntity progress = progressRepository
                .findByUserIdAndCourseId(userId, courseId)
                .orElse(CourseProgressEntity.builder()
                        .userId(userId)
                        .courseId(courseId)
                        .completedLectureIds(new ArrayList<>())
                        .lastUpdated(LocalDateTime.now())
                        .build());

        if (!progress.getCompletedLectureIds().contains(lectureId)) {
            progress.getCompletedLectureIds().add(lectureId);
            progress.setLastUpdated(LocalDateTime.now());
            progressRepository.save(progress);
        }
       return CourseProgressdto.fromEntity(progress);

    }
}
