package com.example.edtech.controller;

import com.example.edtech.config.UserPrincipal;
import com.example.edtech.dto.CommentTreeDto;
import com.example.edtech.dto.Commentdto;
import com.example.edtech.dto.CreateCommentRequest;
import com.example.edtech.dto.Replydto;
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
@RequestMapping("/courses/{courseId}?comments")
public class CourseController {
    @Autowired
    CommentService commentService;

//Top level comment
@Operation(summary = "To add a comment")
    @PostMapping
    public ResponseEntity<Commentdto>addComment(
            @Parameter(example = "")
            @PathVariable String courseId,
            @Parameter(example = "")
            @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal user
){
    ObjectId courseId1=new ObjectId(courseId);
   ObjectId userId=new ObjectId(user.getId());
    Commentdto commentdto = commentService.addComment(courseId1, userId, request.getContent());
    return ResponseEntity.ok(commentdto);
}

    @Operation(summary = "To add reply to the comment")
    @PostMapping("/{parentCommentId}/reply")
    public ResponseEntity<Replydto>addReply(
            @Parameter(example = "")
            @PathVariable String courseId,
            @Parameter(example = "")
            @PathVariable String parentCommentId,
            @Parameter(example = "")
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
    @GetMapping
    public ResponseEntity<List<CommentTreeDto>> getComments(@PathVariable String courseId) {
        return ResponseEntity.ok(commentService.getCommentsTree(courseId));
    }



}
