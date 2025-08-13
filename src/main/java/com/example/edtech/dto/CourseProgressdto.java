package com.example.edtech.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CourseProgressdto {
private String id;
    private String userId;
    private String courseId;
    private List<String> completedLectureIds;

}
