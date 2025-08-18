package com.example.edtech.controller;


import com.example.edtech.entity.CommentEntity;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

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
    private  final BlockedCommentService blockedCommentService;

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
            @PathVariable  String id
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
    @GetMapping("{userId}/lock")
    public ResponseEntity<?>lockUser(
            @Parameter(name = "",required = true)
            @PathVariable  String id
    ){
        try {
            ObjectId userId=new ObjectId(id);

            UserEntity user =userService.findUserById(userId);
            if(user==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            boolean isLocked =userService.lockUser(user.getId());
            if(!isLocked)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to lock the user.");
            return  ResponseEntity.ok().body("User locked successfully");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }

    @Operation(summary = "To unlock the account")
    @GetMapping("{userId}/unlock")
    public ResponseEntity<?>unlockUser(
            @Parameter(name = "",required = true)
            @PathVariable String id
    ){
        try {
            ObjectId userId=new ObjectId(id);

            UserEntity user =userService.findUserById(userId);
            if(user==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            boolean isSuccessfull =userService.unlockUser(user.getId());
            if(!isSuccessfull)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unlock the user.");
            return  ResponseEntity.ok().body("User unlocked successfully");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }

    @Operation(summary = "To disable the account")
    @GetMapping("{userId}/disable")
    public ResponseEntity<?>disableUser(
            @Parameter(name = "",required = true)
            @PathVariable String id
    ){
        try {
            ObjectId userId=new ObjectId(id);

            UserEntity user =userService.findUserById(userId);
            if(user==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            boolean isSuccessfull =userService.disableUser(user.getId());
            if(!isSuccessfull)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to disable the user.");
            return  ResponseEntity.ok().body("User disabled successfully");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }



    @Operation(summary = "To enable the account")
    @GetMapping("{userId}/enable")
    public ResponseEntity<?>enableUser(
            @Parameter(name = "",required = true)
            @PathVariable String id
    ){
        try {
            ObjectId userId=new ObjectId(id);

            UserEntity user =userService.findUserById(userId);
            if(user==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            boolean isSuccessfull =userService.enableUser(user.getId());
            if(!isSuccessfull)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to  enable the user.");
            return  ResponseEntity.ok().body(" User enabled successfully");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }




    @Operation(summary = "To block the user from comment")
    @PutMapping("{courseId}/block/{userId}")
    public ResponseEntity<?>blockUserFromComment(
            @Parameter(name = "",required = true)
            @PathVariable String id1,
            @Parameter(name = "",required = true)
            @PathVariable String id

    ){
        try {
            ObjectId userId=new ObjectId(id);

            UserEntity user =userService.findUserById(userId);
            if(user==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            ObjectId courseId=new ObjectId(id1);
            CourseEntity course = courseService.getCourseByID(courseId);
            if(course==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            boolean isSuccessfull =blockedCommentService.blockUserFromComment(course,user);
            if(!isSuccessfull)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to  block the user from commenting.");
            return  ResponseEntity.ok().body(" User is blocked from commenting on this course");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }


    @Operation(summary = "To unblock the user from comment")
    @PutMapping("{courseId}/unblock/{userId}")
    public ResponseEntity<?>unblockUserFromComment(
            @Parameter(name = "",required = true)
            @PathVariable String id1,
            @Parameter(name = "",required = true)
            @PathVariable String id

    ){
        try {
            ObjectId userId=new ObjectId(id);

            UserEntity user =userService.findUserById(userId);
            if(user==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            ObjectId courseId=new ObjectId(id1);
            CourseEntity course = courseService.getCourseByID(courseId);
            if(course==null)
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            boolean isSuccessfull =blockedCommentService.blockUserFromComment(course,user);
            if(!isSuccessfull)
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to  block the user from commenting.");
            return  ResponseEntity.ok().body(" User is blocked from commenting on this course");
        }
        catch (IllegalArgumentException exception){
            throw  new RuntimeException("Wrong id format");
        }
    }
}
//    Todo:To delete the courses(Admin) =>Done
//    Todo: To delete the user and the teachers =>Done
//    Todo: To delete the courses and the lectures of the teacher => Done
//    Todo:  To lock and unlock the account of user and the teacher => Done
//    Todo: To delete the comments => Done
//    Todo: To block the user from the commenting on the course
//



