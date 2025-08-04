package com.example.edtech.repository;

import com.example.edtech.dto.Userdto;
import com.example.edtech.entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
    UserEntity findByEmail(String email);
// UserEntity save(UserEntity user);

}

