package com.example.edtech.controller;


import com.example.edtech.entity.CommentEntity;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.service.CommentService;
import com.example.edtech.service.CourseService;
import com.example.edtech.service.LectureService;
import com.example.edtech.service.UserService;
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
    private final UserService userService;

    @Operation(summary = "To delete the course")

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?>deleteCourse(
            @Parameter(name = "",required = true)
            @RequestParam String id){
        try {
            ObjectId courseId=new ObjectId(id);
            boolean isCourseExists = courseService.isCourseExistsById(courseId);
          if(!isCourseExists)
              return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");

            boolean isDeleted = courseService.deleteCourse(courseId);
if(!isDeleted)
      return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Course not deleted");
            return  ResponseEntity.ok().body("Course deleted successfully");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }

    @Operation(summary = "To delete the comment")
    @GetMapping("/{courseId}/deleteComment/{commentId}")
    public ResponseEntity<?>deleteComment(
            @Parameter(name = "",required = true)
            @RequestParam String id,
     @Parameter(name = "",required = true)
     @RequestParam String cId
    ){
        try {
            ObjectId courseId=new ObjectId(id);
            ObjectId commentId=new ObjectId(cId);
            boolean isCourseExists = courseService.isCourseExistsById(courseId);
            if(!isCourseExists)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");

            boolean isDeleted = commentService.deleteComment(courseId,commentId,true);
            if(!isDeleted)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Comment not deleted");
            return  ResponseEntity.ok().body("Comment deleted successfully");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }




    @Operation(summary = "To delete the user")
    @GetMapping("user/delete/{userId}")
    public ResponseEntity<?>deleteUser(
            @Parameter(name = "",required = true)
            @RequestParam String id
    ){
        try {
            ObjectId userId=new ObjectId(id);

            UserEntity user =userService.findUserById(userId);

            if(user==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            boolean isDeleted =userService.deleteUser(user);
            if(!isDeleted)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Comment not deleted");
            return  ResponseEntity.ok().body("Comment deleted successfully");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }

    @Operation(summary = "To lock the account")
    @GetMapping("user/lock/{userId}")
    public ResponseEntity<?>lockUser(
            @Parameter(name = "",required = true)
            @RequestParam String id
    ){
        try {
            ObjectId userId=new ObjectId(id);

            UserEntity user =userService.findUserById(userId);

            if(user==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            boolean isDeleted =userService.deleteUser(user);
            if(!isDeleted)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Comment not deleted");
            return  ResponseEntity.ok().body("Comment deleted successfully");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }


}
//    Todo:To delete the courses(Admin) =>Done
//    Todo: To delete the user and the teachers =>Done
//    Todo: To delete the courses and the lectures of the teacher => Done
//    Todo:  To lock and unlock the account of user and the teacher
//    Todo: To delete the comments => Done
//    Todo: To block the user from the commenting on the course
//

