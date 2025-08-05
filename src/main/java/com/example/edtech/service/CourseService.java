package com.example.edtech.service;

import com.example.edtech.dto.Coursedto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.repository.CourseRepository;
import com.example.edtech.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;

    public boolean save(Coursedto dto){
        CourseEntity course=CourseEntity.builder().title(dto.getTitle()).description(dto.getDescription()).category(dto.getCategory()).thumbnailUrl(dto.getThumbnailUrl()).build();
        try{
            course.setCreatedAt(LocalDateTime.now());
            course.setUpdatedAt(LocalDateTime.now());

            course.setCreatedBy(getCurrentUserId());
           Objects.requireNonNull(courseRepository.save(course));

            return true;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return  false;
        }
    }

    private ObjectId getCurrentUserId() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserEntity byEmail = userRepository.findByEmail(authentication.getName());
        return  byEmail.getId();
    }

    public boolean isExistsByTitle(String title){
        return courseRepository.existsByTitle(title);
    }

    public boolean deleteCourse(String id) {
        ObjectId objectId=new ObjectId(id);
        courseRepository.deleteById(objectId);
        return true;
    }
    public  boolean isCourseExistsById(ObjectId id){
        return courseRepository.existsById(id);
    }
}
