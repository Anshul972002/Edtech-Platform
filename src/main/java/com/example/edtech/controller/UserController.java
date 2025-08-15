package com.example.edtech.controller;

import com.example.edtech.config.UserPrincipal;
import com.example.edtech.dto.Coursedto;
import com.example.edtech.dto.LectureReplydto;
import com.example.edtech.dto.Userdto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.entity.UserEntity;
import com.example.edtech.repository.UserRepository;
import com.example.edtech.service.CourseService;
import com.example.edtech.service.FileUploadService;
import com.example.edtech.service.LectureService;
import com.example.edtech.service.UserService;
import com.example.edtech.util.CloudinaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "User Api",description = "Endpoint for user-related operations")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

 private  final  UserService userService;

private  final  CourseService courseService;

private  final  UserRepository userRepository;

private  final  PasswordEncoder passwordEncoder;

private  final  LectureService lectureService;
private final FileUploadService fileUploadService;
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
                                                      @RequestParam(value = "file",required = false) MultipartFile image) throws IOException {

        String fileUrl;

if(name==null && dob==null && address==null && image==null)
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty values");

        // Create Userdto object manually
        UserEntity user = userService.findUserById(new ObjectId(userService.getId()));

        if (name != null && !name.isEmpty() && !user.getName().equals(name)) {
            user.setName(name);
        }

        if (dob != null && !dob.isEmpty() && !user.getDob().equals(dob)) {
            user.setDob(dob);
        }

        if (address != null && !address.isEmpty() && !user.getAddress().equals(address)) {
            user.setAddress(address);
        }

        if (image != null && !image.isEmpty()) {
            CloudinaryResponse cloudinaryResponse = fileUploadService.uploadFile(image);

            user.setImageUrl(cloudinaryResponse.getUrl());
            // Handle image file saving here if needed
            // user.setImagePath(fileName); // optional field
        }
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
           List<CourseEntity> courses = userService.getEnrolledCourses(courseIds);
           Set<ObjectId> foundIds = courses.stream()
                   .map(CourseEntity::getId)
                   .collect(Collectors.toSet());
//           CourseEntity::getId
//           is equivalent to:
//           (course) -> course.getId()

           List<ObjectId> missingIds = courseIds.stream()
                   .filter(id -> !foundIds.contains(id))

                   .collect(Collectors.toList());

           if (missingIds.isEmpty())
               return  ResponseEntity.ok(Map.of("courses",courses));
           else
               return  ResponseEntity.ok(Map.of("courses",courses,"missingIds",missingIds));
       }
    }

//    To enroll in the course
//    Todo:To add the payment gateway for the paid courses
@Operation(summary = "To enroll in the course")
    @PostMapping("/courses/{id}/enroll")
    public ResponseEntity<?>enrollInCourses(
            @Parameter(description = "Id of course to enroll",example = "68918c0fcda0006027078205")
            @PathVariable String id){

        ObjectId objectId=new ObjectId(id);
        UserEntity user = userService.getProfile();
//        TO check if the user is already enrolled and if the course already exist;
//    Check if it is published
        CourseEntity courseByID = courseService.getCourseByID(objectId);

        List<ObjectId>enrolledUser=courseByID.getEnrolledUser();
        if (enrolledUser.contains(user.getId()))
            throw new IllegalStateException("User already enrolled in this course");
   if (!courseByID.isPublished())
       throw new RuntimeException("Course is not published yet");
   else {
       return ResponseEntity.ok("Enrolled successfully");
   }
    }


    @Operation(summary = "To access the course")
    @GetMapping("/courses/{id}")
    public ResponseEntity<?> getCourse(
            @Parameter(description = "Id of course to fetch", example = "68918c0fcda0006027078205")
            @PathVariable String id) {

        try {
            ObjectId objectId = new ObjectId(id);
            CourseEntity courseByID = courseService.getCourseByID(objectId);

            // If free, return lectures
            if (!courseByID.isPaid()) {
                List<LectureReplydto> lectures = lectureService.getLectures(objectId);
                return ResponseEntity.ok(lectures);
            }

            // For paid courses, check if user is enrolled
            UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            boolean enrolled = courseByID.getEnrolledUser().stream()
                    .anyMatch(userId -> userId.toHexString().equals(principal.getId()));

            if (!enrolled) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You have not enrolled in the course");
            }

            List<LectureReplydto> lectures = lectureService.getLectures(objectId);
            return ResponseEntity.ok(lectures);

        } catch (IllegalArgumentException e) { // For invalid ObjectId
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid course ID format");
        }
    }


//    To update the profile


//    Todo: To publish the course(Teacher Controller) => Done
//    Todo: To add paid and free courses option(Teacher Controller) =>Done
//    Todo:Add the functionality of the paid and free courses(Any one can see the lectures of free courses but not for the paid courses) =>Done
//    Todo:To add the progress bar(User Controller) => Done
//    Todo:To upload the videos to the service => Done
//    Todo: To upload the profile photos => Done
//    Todo:To implement the forgot password =>
//    Todo:To add the videoStreaming(Advanced)
//    Todo:To add the live chat (Advanced)

//  Leave the admin for now

//

}




// forgot password

//course module

