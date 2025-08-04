package com.example.edtech.controller;


import com.example.edtech.dto.Userdto;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.service.JWTService;
import com.example.edtech.service.UserService;
import com.example.edtech.util.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

@Tag(name = "Registration api",description = "Register yourself")
@RestController
@RequestMapping("/")
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;
@SecurityRequirement(name = "bearerAuth")
@Operation(summary = "Simple protected api ")
    @GetMapping("/hello")
    private String hello(){
        return "Hello world";
    }

    @Operation(summary = "Register api")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam("name") String name,
            @RequestParam("dob") String dob,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmpassword") String confirmpassword,
            @RequestParam("file") MultipartFile image) {

        String fileName = image.getOriginalFilename();

        // Fixed the password validation logic
        if (!password.equals(confirmpassword)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Password does not match"));
        }

        // Create Userdto object manually
        Userdto user = new Userdto();
        user.setName(name);
        user.setDob(dob);
        user.setAddress(address);
        user.setEmail(email);
        user.setPassword(password);
        user.setConfirmpassword(confirmpassword);
        String password1=user.getPassword();
        String confirmpassword1=user.getConfirmpassword();
        if (userService.findemailexist(user.getEmail()))
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(Map.of("message", "User already exist"));
        boolean save = userService.save(user);
        // your logic here
        if(!save)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not saved"));
   else
            return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }



    @Operation(summary = "Login a user and get JWT")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@Valid @RequestBody LoginUser user) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            Authentication authenticate = authManager.authenticate(token);
            boolean authenticated = authenticate.isAuthenticated();
            if (authenticated)
//                return ResponseEntity.status(HttpStatus.OK).body(Map.of("token",jwtService.generateToken(user.getEmail())));
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Login sucessfull","token",jwtService.generateToken(user.getEmail())));
            else
                throw new UsernameNotFoundException("Invalid user request!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Login failed"));
        }
    }
}
//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnNodWxAMjAwMyIsImlhdCI6MTc1NDI4MDE1NywiZXhwIjoxNzU0MjgwNDU3fQ.NcyZLwSGBXxPOMW5ym-95A0TG8KCje1Y7-d5PmkWdnQ