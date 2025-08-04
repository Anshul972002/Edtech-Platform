package com.example.edtech.service;

import com.example.edtech.dto.Coursedto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.repository.CourseRepository;
import com.example.edtech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CourseService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;

    public boolean save(Coursedto dto){
        CourseEntity course=CourseEntity.builder().title(dto.getTitle()).description(dto.getDescription()).category(dto.getCategory()).thumbnailUrl(dto.getThumbnailUrl()).build();
        try{
            Objects.requireNonNull(courseRepository.save(course));
        return true;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return  false;
        }
    }
}
