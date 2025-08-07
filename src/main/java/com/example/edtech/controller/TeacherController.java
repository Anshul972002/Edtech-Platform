package com.example.edtech.controller;

import com.example.edtech.dto.Coursedto;
import com.example.edtech.dto.Lecturedto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.repository.CourseRepository;
import com.example.edtech.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Teacher api",description = "Endpoint for the teacher related api")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    CourseService courseService;
    @Autowired

    CourseRepository courseRepository;





    @GetMapping
    public String hello(){
        return "Hello from the teachers";
    }


//    Courses
    @Operation(summary = "Create new Course")
    @PostMapping("/courses")
    public ResponseEntity<Map<String,Object>>courses(@Valid @RequestBody Coursedto course){
        if (courseService.isExistsByTitle(course.getTitle())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Course title already exists"));
        }
        if (!course.getThumbnailUrl().startsWith("http") ||!course.getThumbnailUrl().startsWith("https") ) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid thumbnail URL"));
        }
if (courseService.save(course))
        return ResponseEntity.ok(Map.of("message","Sucessfully creted the course"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","Sucessfully creted the course"));
    }

    @Operation( summary =  "Edit the course")
    @PutMapping("/courses/{id}")
    public ResponseEntity<?> updateCourse( @Parameter(
            description = "ID of the course to edit",
            example = "68918c0fcda0006027078205"
    )@PathVariable String id, @RequestBody Coursedto dto) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(new ObjectId(id));

        if (optionalCourse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Course not found"));
        }

        CourseEntity course = optionalCourse.get();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setCategory(dto.getCategory());
        course.setThumbnailUrl(dto.getThumbnailUrl());
        course.setUpdatedAt(LocalDateTime.now());
        course.setCreatedBy(courseService.getCurrentUserId());

        courseRepository.save(course);

        return ResponseEntity.ok(Map.of("message", "Course updated successfully"));
    }

    @Operation(
            summary = "Delete a course by ID",
            description = "Deletes a course if it exists. Returns 200 if successful, or 404 if not found."
    )
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> deleteCourse( @Parameter(
            description = "ID of the course to be deleted",
            example = "68918c0fcda0006027078205"
    )@PathVariable String id) {
        try {
            ObjectId courseId = new ObjectId(id);

            boolean exists = courseService.isCourseExistsById(courseId);
            if (!exists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No course found with this ID"));
            }

            courseService.deleteCourse(id);
            return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));

        } catch (IllegalArgumentException e) {
            // ObjectId constructor throws this if id is not a valid 24-char hex string
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid course ID format"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error deleting course", "error", e.getMessage()));
        }
    }

//    Adding the lecture to the course
//    First of all we have to deal with the duplicatoin
  @Operation(summary = "Add the lecture to the course")
    @PostMapping("/courses/{id}/lecture")
    public ResponseEntity<?>addLectureToCourse(
          @Parameter(
                  description = "ID of the course to add the lecture",
                  example = "68918c0fcda0006027078205"
          )
            @PathVariable String id, @RequestBody Lecturedto lecture){
// The video transcoding is left meaning converting the video in to the different resolutions
try {
    ObjectId objectId=new ObjectId(id);
    CourseEntity course = courseRepository.findById(objectId).orElseThrow(() -> new RuntimeException("Course not found"));
//    Check if the user is valid user
    boolean validUserOfCourse = courseService.isValidUserOfCourse(course);
    if(!validUserOfCourse)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "You are not the valid user of the course"));
//Save the lecture to the course
    boolean isSaved = courseService.saveLectureInCourse(lecture, course);
    if (!isSaved)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Lecture is not saved"));

    return ResponseEntity.status(HttpStatus.OK)
            .body(Map.of("message", "Lecture is added to the course"));

}
catch (IllegalArgumentException e) {
    // ObjectId constructor throws this if id is not a valid 24-char hex string
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", "Invalid course ID format"));
} catch (Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "Error deleting course", "error", e.getMessage()));
}



    }
    @Operation(summary = "To publish the course")
   @PostMapping("/courses/{id}/publish")
    public ResponseEntity<?>publishTheCourse( @Parameter(
            description = "ID of the course to be published",
            example = "68918c0fcda0006027078205"
    )
            @PathVariable String id) throws AccessDeniedException {
try {
    ObjectId objectId=new ObjectId(id);
    CourseEntity course=courseRepository.findById(objectId).orElseThrow(()->new RuntimeException("Course not found"));
    if(courseService.isValidUserOfCourse(course)){
        course.setPublished(true);
        course.setUpdatedAt(LocalDateTime.now());
        CourseEntity save = courseRepository.save(course);
        return ResponseEntity.ok(Map.of("message","Course is published")) ;
}
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Course is published")) ;
}
catch (Exception exception){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Give the valid id")) ;
}

    }

//    To add the progress tracker

//   To get Enrollerd Students

}
