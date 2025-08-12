package com.example.edtech.service;

import com.example.edtech.config.UserPrincipal;
import com.example.edtech.dto.CourseReplydto;
import com.example.edtech.dto.Coursedto;
import com.example.edtech.dto.Lecturedto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.LectureEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.repository.CourseRepository;
import com.example.edtech.repository.LectureRepository;
import com.example.edtech.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CourseService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    LectureRepository lectureRepository;

    public boolean save(Coursedto dto) {
        CourseEntity course = CourseEntity.builder().title(dto.getTitle()).description(dto.getDescription()).category(dto.getCategory()).thumbnailUrl(dto.getThumbnailUrl()).build();
        try {
            course.setCreatedAt(LocalDateTime.now());
            course.setUpdatedAt(LocalDateTime.now());
           course.setPublished(false);
           course.setPaid(false);
            course.setCreatedBy(getCurrentUserId());
            Objects.requireNonNull(courseRepository.save(course));
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public ObjectId getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity byEmail = userRepository.findByEmail(authentication.getName());
        return byEmail.getId();
    }

    public boolean isExistsByTitle(String title) {
        return courseRepository.existsByTitle(title);
    }

    public boolean deleteCourse(String id) {
        ObjectId objectId = new ObjectId(id);
        lectureRepository.deleteByCourseId(objectId);
        courseRepository.deleteById(objectId);
        return true;
    }

    public boolean isCourseExistsById(ObjectId id) {
        return courseRepository.existsById(id);
    }

    public boolean isValidUserOfCourse(CourseEntity course) throws AccessDeniedException {
        if (!course.getCreatedBy().toHexString().equals(getCurrentUserId().toHexString()))
            throw new AccessDeniedException("You are not allowed to modify this course");
        return true;
    }

    public boolean saveLectureInCourse(Lecturedto lecture,CourseEntity course) {
        String videoUrl=lecture.getVideoUrl();
        if (videoUrl == null || (!videoUrl.startsWith("http://") && !videoUrl.startsWith("https://"))) {
            throw new IllegalArgumentException("Invalid video URL. It must start with http or https.");
        }
        LectureEntity lectureEntity=LectureEntity.toEntity(lecture);
        lectureEntity.setCourseID(course.getId());
        LectureEntity savedLecture = lectureRepository.save(lectureEntity);
        if (course.getLectureId() == null) {
            course.setLectureId(new ArrayList<>());
        }
        course.getLectureId().add(savedLecture.getId());
        course.setUpdatedAt(LocalDateTime.now());
        courseRepository.save(course);
        return true;
    }

    public Page<CourseReplydto> getAllActiveCourses(int page,int size) {
        Pageable pageable= PageRequest.of(page,size);
        Page<CourseEntity> courses = courseRepository.findByIsPublishedTrue(pageable);
        Page<CourseReplydto> coursereplydto = courses.map(CourseReplydto::fromEntity);
        return coursereplydto;

    }

    public Page<CourseReplydto> getAllCourses(int page,int size) {
        Pageable pageable= PageRequest.of(page,size);
        Page<CourseEntity> courses = courseRepository.findAll(pageable);
        Page<CourseReplydto> coursereplydto = courses.map(CourseReplydto::fromEntity);
        return coursereplydto;

    }

    public CourseEntity getCourseByID(ObjectId id) {
        CourseEntity course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("No course is found"));
       return course;
    }

    public Page<CourseReplydto> getFreeCourses(int page, int size) {
        UserPrincipal userPrincipal=(UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String teacherId = userPrincipal.getId();

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CourseEntity> courses = courseRepository.findByCreatedByAndIsPaidFalse(teacherId, pageRequest);
          return courses.map(CourseReplydto::fromEntity);

    }


    public Page<CourseReplydto> getPaidCourses(int page, int size) {
        UserPrincipal userPrincipal=(UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String teacherId = userPrincipal.getId();

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CourseEntity> courses = courseRepository.findByCreatedByAndIsPaidTrue(teacherId, pageRequest);
        return courses.map(CourseReplydto::fromEntity);

    }
//    Pageable pageable = PageRequest.of(page, size);
//    is used to create a pagination request in Spring Data (both JPA and MongoDB).
//
//            🔍 What is Pageable?
//    Pageable is an interface provided by Spring Data that represents pagination information:
//
//    Which page you want to fetch (e.g. page 0, page 1, etc.)
//
//    How many items per page you want to retrieve
//
//            (Optional) Sorting rules
//
//🔍 What is PageRequest.of(page, size)?
//    PageRequest is a concrete class that implements Pageable.
//
//            page: The page number you want to fetch (starts from 0, not 1).
//
//    size: The number of items (documents/rows) per page.
//
//    Example:
//
//    java
//    Copy code
//    Pageable pageable = PageRequest.of(1, 10);
//    This means:
//            → Get page 1,
//            → With 10 records per page.
//
//            Internally, it will tell Spring Data to skip the first 10 results (i.e., page * size) and fetch the next 10.

}
