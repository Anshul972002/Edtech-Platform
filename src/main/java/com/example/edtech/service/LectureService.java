package com.example.edtech.service;

import com.example.edtech.dto.LectureReplydto;
import com.example.edtech.dto.Lecturedto;
import com.example.edtech.entity.LectureEntity;
import com.example.edtech.repository.LectureRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LectureService {
    @Autowired
    LectureRepository lectureRepository;


    public boolean saveLectures(Lecturedto lecturedto) {
        LectureEntity lectureEntity = LectureEntity.toEntity(lecturedto);
        try {
            LectureEntity save = ObjectUtils.requireNonEmpty(lectureRepository.save(lectureEntity));
        return  true;
        }
      catch (Exception exception){
            throw new RuntimeException("Lecture not saved");
      }

    }
    public List<LectureReplydto> getLectures(ObjectId courseId) {
        List<LectureEntity> lectures = lectureRepository.findByCourseId(courseId);
        List<LectureReplydto> lectureReplydto = lectures.stream().map(LectureReplydto::fromEntity).collect(Collectors.toList());
return lectureReplydto;
    }
}
