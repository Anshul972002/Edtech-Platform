package com.example.edtech.controller;

import com.example.edtech.config.UserPrincipal;
import com.example.edtech.dto.*;
import com.example.edtech.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Course Api")
@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    CommentService commentService;

//Top level comment


    GetMapping
    public ResponseEntity<Coursedto>

@Operation(summary = "To add a comment")
    @PostMapping("/{courseId}/comments")
    public ResponseEntity<Commentdto>addComment(
            @Parameter(example = "68918c0fcda0006027078205")
            @PathVariable String courseId,
            @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal user
){
    ObjectId courseId1=new ObjectId(courseId);
   ObjectId userId=new ObjectId(user.getId());
    Commentdto commentdto = commentService.addComment(courseId1, userId, request.getContent());
    return ResponseEntity.ok(commentdto);
}

    @Operation(summary = "To add reply to the comment")
    @PostMapping("/{courseId}/comments/{parentCommentId}/reply")
    public ResponseEntity<Replydto>addReply(
            @Parameter(example = "68918c0fcda0006027078205")
            @PathVariable String courseId,
          @Parameter(example = "68918c0fcda0006027078205")
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
            @Parameter(example = "68918c0fcda0006027078205")
            @PathVariable String courseId) {
        return ResponseEntity.ok(commentService.getCommentsTree(new ObjectId(courseId)));
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

    @Operation(summary = "To get reply of the paticular comment")
    @GetMapping("/{courseId}/comments/{parentCommentId}/reply")
    public ResponseEntity<List<Commentdto>> getReply(
            @Parameter(example = "68918c0fcda0006027078205")
            @PathVariable String courseId,
          @Parameter(example = "68918c0fcda0006027078205")
         @PathVariable String parentCommentId,
            @Parameter(example = "0")
            @RequestParam int page,
            @Parameter(example = "10")
            @RequestParam  int size
    ) {
        return ResponseEntity.ok(commentService.getReplies(new ObjectId(courseId),new ObjectId(parentCommentId),page,size).getContent());
    }

}
