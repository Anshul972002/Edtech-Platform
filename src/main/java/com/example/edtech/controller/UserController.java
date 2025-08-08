package com.example.edtech.controller;

import com.example.edtech.dto.Userdto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.repository.UserRepository;
import com.example.edtech.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "User Api",description = "Endpoint for user-related operations")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("user")
public class UserController {
@Autowired
    UserService userService;
@Autowired
    UserRepository userRepository;
@Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/hello")
    public String hello(){
    return "Hi";
    }

    // profile management
    @Operation(summary = "To get the profile of the user")
    @GetMapping("/profile")
    public ResponseEntity<Userdto> userProfie(){
        UserEntity profile = userService.getProfile();
        Userdto profiledto=Userdto.builder().name(profile.getName()).dob(profile.getDob()).email(profile.getEmail()).address(profile.getAddress()).build();
        return  ResponseEntity.ok(profiledto);
    }
    @Operation(summary = "To update the profile")
    @PostMapping("/profile")
    public ResponseEntity<?> updateUserProfie(  @RequestParam(value = "name",required = false) String name,
                                                      @RequestParam(value = "dob",required = false) String dob,
                                                      @RequestParam(value = "address",required = false) String address,
                                                      @RequestParam(value = "file",required = false) MultipartFile image) {

        String fileName = image.getOriginalFilename();

if(name==null && dob==null && address==null && image==null)
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty values");

        // Create Userdto object manually
        UserEntity user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());

        if (name != null && !name.isEmpty() && !user.getName().equals(name)) {
            user.setName(name);
        }

        if (dob != null && !dob.isEmpty() && user.getDob().equals(dob)) {
            user.setDob(dob);
        }

        if (address != null && !address.isEmpty()) {
            user.setAddress(address);
        }

//        if (image != null && !image.isEmpty()) {
//            String fileName = image.getOriginalFilename();
//            // Handle image file saving here if needed
//            // user.setImagePath(fileName); // optional field
//        }
userRepository.save(user);
        // return updated user as a response
        return ResponseEntity.ok(user);
    }

//    TO get the enrolled courses
    @Operation(summary = "To get the enrolled courses")
    @GetMapping("/course")
    public ResponseEntity<?> enrolledCourses(){
        UserEntity user = userService.getProfile();
       if(user.getEnrolledCourses()==null){
           return ResponseEntity.ok("No courses found");
       }
       else {
           List<ObjectId>courseIds=user.getEnrolledCourses();
           List<CourseEntity> courses = userService.getEnrolledCourses(user.getEnrolledCourses());
           Set<ObjectId> foundIds = courses.stream()
                   .map(CourseEntity::getId)
                   .collect(Collectors.toSet());
//           CourseEntity::getId
//           is equivalent to:
//           (course) -> course.getId()

           List<ObjectId> missingIds = courseIds.stream()
                   .filter(id -> !foundIds.contains(id))

                   .collect(Collectors.toList());

           if (missingIds==null)
               return  ResponseEntity.ok(Map.of("courses",courses));
           else
               return  ResponseEntity.ok(Map.of("courses",courses,"missingIds",missingIds));
       }
    }

//    To enroll in the course
    @PostMapping("/courses/{id}/enroll")
    public ResponseEntity<?>enrollInCourses(
            @Parameter(description = "Id of course to enroll",example = "68918c0fcda0006027078205")
            @PathVariable String id){

    }
}




// forgot password

//course module

