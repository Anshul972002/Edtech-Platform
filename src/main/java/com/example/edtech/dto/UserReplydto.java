package com.example.edtech.dto;

import com.example.edtech.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReplydto {
    private String id;


    private String name;

     private String dob;


    private String address;

    private String email;

    private Map<String,String >profile;

    public static UserReplydto fromEntity(UserEntity userEntity){
      return  UserReplydto.builder()
                .id(userEntity.getId().toHexString())
                 .name(userEntity.getName())
                 .dob(userEntity.getDob())
                 .address(userEntity.getAddress())
                 .email(userEntity.getEmail())
                 .profile(userEntity.getProfile())
                 .build();
    }
}