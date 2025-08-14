package com.example.edtech.controller;


import com.cloudinary.Cloudinary;
import com.example.edtech.config.UserPrincipal;
import com.example.edtech.dto.CourseReplydto;
import com.example.edtech.dto.Coursedto;
import com.example.edtech.dto.Userdto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.repository.CourseRepository;
import com.example.edtech.service.CourseService;
import com.example.edtech.service.FileUploadService;
import com.example.edtech.service.JWTService;
import com.example.edtech.service.UserService;
import com.example.edtech.util.CloudinaryResponse;
import com.example.edtech.util.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Tag(name = "Home api",description = "Accessible apis without the authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

   private final UserService userService;

    private final AuthenticationManager authManager;

    private final JWTService jwtService;
    private final CourseService courseService;
    private final FileUploadService fileUploadService;
    private final Cloudinary cloudinary;
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
            @RequestParam("file") MultipartFile image) throws IOException {



        try {
            CloudinaryResponse cloudinaryResponse = fileUploadService.uploadFile(image);

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
            boolean save = userService.save(user,cloudinaryResponse.getUrl());
            // your logic here
            if(!save){
                fileUploadService.deleteFile(cloudinaryResponse.getId());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not saved"));
            }

            else
                return ResponseEntity.ok(Map.of("message", "Registered successfully"));
        }
        catch (IOException exception){
            System.out.println(exception.getMessage());
            throw new IOException("Imaged not saved to the server");

        }

        }



    @Operation(summary = "Login a user and get JWT")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@Valid @RequestBody LoginUser user) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail().toLowerCase().trim(), user.getPassword());
            Authentication authenticate = authManager.authenticate(token);
            boolean authenticated = authenticate.isAuthenticated();
            if (authenticated){
//                return ResponseEntity.status(HttpStatus.OK).body(Map.of("token",jwtService.generateToken(user.getEmail())));
                UserPrincipal userPrincipal=(UserPrincipal) authenticate.getPrincipal();
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Login sucessfull","token",jwtService.generateToken(userPrincipal)));
            }

            else
                throw new UsernameNotFoundException("Invalid user request!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Login failed"));
        }
    }


//    Courses API
    @Operation(summary = "List of all the published courses")
    @GetMapping("/courses")
    public ResponseEntity<List<CourseReplydto>>getAllActiveCourses(@Parameter(example = "0") @RequestParam(defaultValue = "0")int page, @Parameter(description = "Number of items per page",example = "10")@RequestParam(defaultValue = "10") int size){
        Page<CourseReplydto> allActiveCourses = courseService.getAllActiveCourses(page, size);

        return ResponseEntity.ok(allActiveCourses.getContent());
    }

    @Operation(summary = "List of all  courses")
    @GetMapping("/courses")
    public ResponseEntity<List<CourseReplydto>>getAllCourses(@Parameter(example = "0") @RequestParam(defaultValue = "0")int page, @Parameter(description = "Number of items per page",example = "10")@RequestParam(defaultValue = "10") int size){
        Page<CourseReplydto> allCourses = courseService.getAllCourses(page, size);

        return ResponseEntity.ok(allCourses.getContent());
    }

    @Operation(summary = "Details of particular course")
    @GetMapping("/courses/{id}?details")
    public ResponseEntity<?>getCourse(
            @Parameter(description = "Id of course to fetch",example = "68918c0fcda0006027078205")
            @PathVariable String id){
    try {
        ObjectId objectId=new ObjectId(id);
        CourseEntity courseByID = courseService.getCourseByID(objectId);

        return ResponseEntity.ok(Coursedto.fromEntity(courseByID));
    }
    catch (Exception exception){
        System.out.println(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
    }



    }






}
//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnNodWxAMjAwMyIsImlhdCI6MTc1NDI4MDE1NywiZXhwIjoxNzU0MjgwNDU3fQ.NcyZLwSGBXxPOMW5ym-95A0TG8KCje1Y7-d5PmkWdnQ