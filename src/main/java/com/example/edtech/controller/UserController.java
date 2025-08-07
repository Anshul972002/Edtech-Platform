package com.example.edtech.controller;

import com.example.edtech.dto.Userdto;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Api",description = "Endpoint for user-related operations")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("user")
public class UserController {
@Autowired
    UserService userService;
@Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/hello")
    public String hello(){
    return "Hi";
    }

    // profile management
    @GetMapping("/profile")
    public ResponseEntity<Userdto> userProfie(){
        UserEntity profile = userService.getProfile();
        Userdto profiledto=Userdto.builder().name(profile.getName()).dob(profile.getDob()).email(profile.getEmail()).address(profile.getAddress()).build();
        return  ResponseEntity.ok(profiledto);
    }

    @PostMapping("/profile")
    public ResponseEntity<Userdto> updateUserProfie(@ResponseBody ){
        UserEntity profile = userService.getProfile();
        Userdto profiledto=Userdto.builder().name(profile.getName()).dob(profile.getDob()).email(profile.getEmail()).address(profile.getAddress()).build();
        return  ResponseEntity.ok(profiledto);
    }
}




// forgot password

//course module

