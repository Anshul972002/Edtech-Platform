package com.example.edtech.dto;

import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.CourseProgressEntity;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
@Data
public class CourseProgressdto {
    private List<String> completedLectureIds;
    private String userId;
    private String courseId;
    private LocalDateTime lastUpdated;

    public static CourseProgressdto fromEntity(CourseProgressEntity progress) {
        return CourseProgressdto.builder()
                .completedLectureIds(
                        progress.getCompletedLectureIds()
                                .stream()
                                .map(ObjectId::toHexString)
                                .toList()
                )
                .userId(progress.getUserId().toHexString())
                .courseId(progress.getCourseId().toHexString())
                .lastUpdated(progress.getLastUpdated())
                .build();
    }
}

//1. collect(Collectors.toList())
//Returns a List (usually ArrayList) that is mutable (you can add/remove elements).
//
//It always creates a new list.
//
//This is the traditional approach before Java 16
// toList() (Java 16+)
//Returns an unmodifiable list — you cannot add/remove elements afterward (will throw UnsupportedOperationException).
