package com.example.edtech.controller;


import com.example.edtech.entity.CommentEntity;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.service.CommentService;
import com.example.edtech.service.CourseService;
import com.example.edtech.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Controller")
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
public class AdminController {
    private final CourseService courseService;
    private final LectureService lectureService;
    private  final CommentService commentService;
    private
    @Operation(summary = "To delete the course")

    @GetMapping("/{courseId}")
    public ResponseEntity<?>deleteCourse(
            @Parameter(name = "",required = true)
            @RequestParam String Id){
        try {
            ObjectId courseId=new ObjectId(id);
            boolean isCourseExists = courseService.isCourseExistsById(courseId);
          if(!isCourseExists)
              return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");

            courseService.deleteCourse(courseId);

        }
        catch (IllegalArgumentException){
            throw  new RuntimeException("Wrong id format");
        }
    }

}
//    Todo:To delete the courses(Admin)
//    Todo: To delete the user and the teachers
//    Todo: To delete the courses and the lectures of the teacher
//    Todo:  To lock and unlock the account of user and the teacher
//    Todo: To delete the comments
//    Todo: To block the user from the commenting on the course
//
