package com.example.edtech.controller;

import com.example.edtech.config.UserPrincipal;
import com.example.edtech.dto.*;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.CourseProgressEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.service.CommentService;
import com.example.edtech.service.CourseProgressService;
import com.example.edtech.service.CourseService;
import com.example.edtech.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('USER')")
@Tag(name = "Course api",description = "Endpoint for the Course related operations")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
   private final CommentService commentService;
   private final CourseService courseService;
   private final CourseProgressService courseProgressService;
   private final UserService userService;

//Top level comment
//@Operation(summary = "Get all courses")
//    @GetMapping
//    public ResponseEntity<List<Coursedto>>getAllCourses(
//            @Parameter(example = "0")
//            @RequestParam int page,
//            @Parameter(example = "10")
//            @RequestParam int size){
//        return ResponseEntity.ok(courseService.getAllCourses(page,size).getContent());
//    }



@Operation(summary = "To add a comment")
    @PostMapping("/{courseId}/comments")
    public ResponseEntity<Commentdto>addComment(
            @Parameter(example = "68a54f0171e5d5ef4b4fc18c")
            @PathVariable String courseId,
            @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal user
)
{
    ObjectId courseId1=new ObjectId(courseId);
    ObjectId userId=new ObjectId(user.getId());
    Commentdto commentdto = commentService.addComment(courseId1, userId, request.getContent());
    return ResponseEntity.ok(commentdto);

}

    @Operation(summary = "To add reply to the comment")
    @PostMapping("/{courseId}/comments/{parentCommentId}/reply")
    public ResponseEntity<Replydto>addReply(
            @Parameter(example = "68a54f0171e5d5ef4b4fc18c")
            @PathVariable String courseId,
          @Parameter(example = "68a61e3885849a3ff2a7d9d3")
         @PathVariable String parentCommentId,
           
            @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ){
        ObjectId courseId1=new ObjectId(courseId);
        ObjectId parentCommentId1=new ObjectId(parentCommentId);
        ObjectId userId=new ObjectId(user.getId());
        Replydto commentdto = commentService.addReply(courseId1,parentCommentId1, userId, request.getContent());
        return ResponseEntity.ok(commentdto);

    }
    @Operation(summary = "To get all the comments with reply")
    @GetMapping("/{courseId}/comments/all")
    public ResponseEntity<List<CommentTreeDto>> getComments(
            @Parameter(example = "68a54f0171e5d5ef4b4fc18c")
            @PathVariable String courseId) {
    try {
        ObjectId courseid =new ObjectId(courseId);
        List<CommentTreeDto> commentsTree = commentService.getCommentsTree(courseid);
        return ResponseEntity.ok(commentsTree);
    }
    catch (IllegalArgumentException e){
        System.out.println(e.getMessage());
        throw  new RuntimeException("Id is not correct");
    }


    }
    @Operation(summary = "To get top level comments only")
    @GetMapping("/{courseId}/comments")
    public ResponseEntity<List<Commentdto>> getTopComments(
            @Parameter(example = "68918c0fcda0006027078205")
            @PathVariable String courseId,
    @Parameter(example = "0")
    @RequestParam int page,
            @Parameter(example = "10")
            @RequestParam  int size
    ) {
        return ResponseEntity.ok(commentService.getTopLevelComments(new ObjectId(courseId),page,size).getContent());
    }

    @Operation(summary = "To get reply of the particular comment")
    @GetMapping("/{courseId}/comments/{parentCommentId}/reply")
    public ResponseEntity<List<Commentdto>> getReply(
            @Parameter(example = "68a54f0171e5d5ef4b4fc18c")
            @PathVariable String courseId,
          @Parameter(example = "68a61e3885849a3ff2a7d9d3")
         @PathVariable String parentCommentId,
            @Parameter(example = "0")
            @RequestParam int page,
            @Parameter(example = "10")
            @RequestParam  int size
    ) {
    try {
        ObjectId courseId1=     new ObjectId(courseId);
        ObjectId parentCommentId1=   new ObjectId(parentCommentId);
        Page<Commentdto> replies = commentService.getReplies(courseId1, parentCommentId1, page, size);
        return ResponseEntity.ok(replies.getContent());
    }
    catch (IllegalArgumentException e){
        System.out.println(e.getMessage());
        throw new RuntimeException("Invalid id");
    }

    }


//    To add the CourseProgress apis
@Operation(summary = "To get the progress of the course")
    @GetMapping("/{courseId}/progress")
    public ResponseEntity<?> getProgress(
        @Parameter(example = "68a54f0171e5d5ef4b4fc18c")
            @PathVariable String courseId) {
    ObjectId courseId1=new ObjectId(courseId);
        CourseEntity courseByID = courseService.getCourseByID(courseId1);
    ObjectId userId = new ObjectId(userService.getId());
    UserEntity users = userService.isEnrolledInCourse(courseId1,userId);
     if (users==null)
         throw new RuntimeException("User is not enrolled in the course");
        CourseProgressEntity progress = courseProgressService.getProgress(userId, courseId1);
        double progressPercentage = courseProgressService.getProgressPercentage(courseByID, progress);
int totalLectures=courseByID.getLectureId().size();
int completedLecture=progress.getCompletedLectureIds().size();
Map<String,Object>map=new HashMap<>();
        map.put("progress",progressPercentage);
        map.put("totalLecture",totalLectures);
        map.put("completedLecture",completedLecture);
        return ResponseEntity.ok(map);
    }
    @Operation(summary = "To change the progress if the lecture is completed")
    @PostMapping("/{courseId}/progress/complete/{lectureId}")
    public ResponseEntity<?> completeLecture(
            @Parameter(example = "")
            @PathVariable String courseId,
            @Parameter(example = "")
            @PathVariable String lectureId) {
        try {
            ObjectId courseId1 = new ObjectId(courseId);
            ObjectId lectureId1 = new ObjectId(lectureId);
            ObjectId userId = new ObjectId(userService.getId());
            return ResponseEntity.ok(courseProgressService.markLectureAsCompleted(userId, courseId1, lectureId1));
        }
        catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide the correct id");
        }
    }

    @Operation(summary = "To toggle like on comments")
    @PostMapping("/comment/{commentId}/toggleLike")
    public ResponseEntity<?> toggleLike(
            @Parameter(example = "")
            @PathVariable String courseId,
            @Parameter(example = "")
            @PathVariable String commentId) {
        try {
            ObjectId courseId1 = new ObjectId(courseId);
            ObjectId commentId1 = new ObjectId(commentId);

            ObjectId userId = new ObjectId(userService.getId());
            int likeCnt = commentService.toggleLike(commentId1, userId);
            return ResponseEntity.ok(Map.of("Like count", likeCnt));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide the correct id");
        }
    }
}
