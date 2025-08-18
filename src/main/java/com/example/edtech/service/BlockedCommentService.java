package com.example.edtech.service;

import com.example.edtech.entity.BlockedCommentEntity;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.repository.BlockedCommentRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlockedCommentService {
   BlockedCommentRepository blockedCommentRepository;
   public boolean blockUserFromComment(CourseEntity course, UserEntity user){
       try {
           ObjectId courseId=course.getId();
           ObjectId userId=user.getId();
           BlockedCommentEntity save = BlockedCommentEntity.builder().userId(userId).courseId(courseId).blockedAt(LocalDateTime.now()).build();
           blockedCommentRepository.save(save);
       return  true;
       }
       catch (Exception e){
           System.out.println(e.getMessage());
           return  false;
       }

   }


    public boolean unblockUserFromComment(CourseEntity course, UserEntity user){
        try {
            ObjectId courseId=course.getId();
            ObjectId userId=user.getId();
           blockedCommentRepository.deleteByCourseIdAndUserId(courseId,userId);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return  false;
        }

    }
}
