package com.example.edtech.controller;

import com.example.edtech.dto.CourseReplydto;
import com.example.edtech.dto.Coursedto;
import com.example.edtech.dto.LectureReplydto;
import com.example.edtech.dto.Lecturedto;
import com.example.edtech.entity.CourseEntity;
import com.example.edtech.repository.CourseRepository;
import com.example.edtech.service.CourseService;
import com.example.edtech.service.FileUploadService;
import com.example.edtech.service.LectureService;
import com.example.edtech.service.VideoUploadService;
import com.example.edtech.util.CloudinaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Teacher api",description = "Endpoint for the Teacher related operations")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {


   private final CourseService courseService;


   private final CourseRepository courseRepository;


    private final LectureService lectureService;

private final VideoUploadService videoUploadService;
private final FileUploadService fileUploadService;



    @GetMapping
    public String hello(){
        return "Hello from the teachers";
    }

    //    Courses
    @Operation(summary = "Create new Course")
//    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value="/create-courses",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?>courses(@Valid @ModelAttribute Coursedto course
            ,@RequestParam(value = "file", required = false) MultipartFile image) throws IOException {
        if (courseService.isExistsByTitle(course.getTitle())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Course title already exists"));
        }
        CloudinaryResponse response=null;
        if(image!=null && !image.isEmpty())
         response = fileUploadService.uploadFile(image,1);
        CourseReplydto save = courseService.save(course, response);
        if (save!=null)
            return ResponseEntity.ok(save);
        else {
            fileUploadService.deleteFile(response.getId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","Failed to create the course"));
        }

    }

    @Operation(summary = "List of all free course")
    @GetMapping("/courses/free")
    public ResponseEntity<List<CourseReplydto>>getFreeCourses(@Parameter(example = "0") @RequestParam(defaultValue = "0")int page, @Parameter(description = "Number of items per page",example = "10")@RequestParam(defaultValue = "10") int size){

        Page<CourseReplydto> allCourses = courseService.getFreeCourses(page, size);

        return ResponseEntity.ok(allCourses.getContent());
    }


    @Operation(summary = "List of all paid course")
    @GetMapping("/courses/paid")
    public ResponseEntity<List<CourseReplydto>>getPaidCourses(@Parameter(example = "0") @RequestParam(defaultValue = "0")int page, @Parameter(description = "Number of items per page",example = "10")@RequestParam(defaultValue = "10") int size){
        Page<CourseReplydto> allCourses = courseService.getPaidCourses(page, size);
        return ResponseEntity.ok(allCourses.getContent());
    }



    @Operation(summary = "To publish the course")
    @PostMapping("/courses/{id}/publish")
    public ResponseEntity<?>publishTheCourse( @Parameter(
            description = "ID of the course to be published",
            example = "68a54f0171e5d5ef4b4fc18c"
    ) @PathVariable String id) throws AccessDeniedException {
        try {
            ObjectId objectId=new ObjectId(id);
            CourseEntity course=courseRepository.findById(objectId).orElseThrow(()->new RuntimeException("Course not found"));
            if(courseService.isValidUserOfCourse(course) ){
                if( course.isPublished())
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Course is already published")) ;
                course.setPublished(true);
                course.setUpdatedAt(LocalDateTime.now());
                CourseEntity save = courseRepository.save(course);
                return ResponseEntity.ok(save);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","You are not valid user")) ;
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Give the valid id")) ;
        }
    }
    @Operation( summary =  "Edit the course")
    @PutMapping(value = "/courses/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCourse( @Parameter(
            description = "ID of the course to edit",
            example = "68a54f0171e5d5ef4b4fc18c"
    )@PathVariable String id, @ModelAttribute Coursedto dto,@RequestParam(value = "file", required = false) MultipartFile image) throws IOException {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(new ObjectId(id));
        if (optionalCourse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Course not found"));
        }
        CloudinaryResponse response=null;
if (image!=null && !image.isEmpty()){
     response = fileUploadService.uploadFile(image);
}
        CourseEntity course = optionalCourse.get();
        course.setTitle(dto.getTitle());
        if (response!=null)
       course.setThumbnail(Map.of("id", response.getId(),"url", response.getUrl()));
        course.setDescription(dto.getDescription());
        course.setCategory(dto.getCategory());
        course.setUpdatedAt(LocalDateTime.now());
        course.setCreatedBy(courseService.getCurrentUserId());
        courseRepository.save(course);
        CourseReplydto courseReplydto = CourseReplydto.fromEntity(course);

        return ResponseEntity.ok(courseReplydto);
    }




    @Operation(
            summary = "Delete a course by ID",
            description = "Deletes a course if it exists. Returns 200 if successful, or 404 if not found."
    )
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> deleteCourse( @Parameter(
            description = "ID of the course to be deleted",
            example = "68a562fa90c529015c59de6c"
    )@PathVariable String id) {
        try {
            ObjectId courseId = new ObjectId(id);

            CourseEntity courseByID = courseService.getCourseByID(courseId);
            if (courseByID==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No course found with this ID"));
            }
            boolean validUserOfCourse = courseService.isValidUserOfCourse(courseByID);
            if (!validUserOfCourse) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "You are not the valid user of the course"));
            }

            courseService.deleteCourse(courseId);
            return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));

        } catch (IllegalArgumentException e) {
            // ObjectId constructor throws this if id is not a valid 24-char hex string
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid course ID format"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error deleting course", "error", e.getMessage()));
        }
    }
//    Adding the lecture to the course
//    First of all we have to deal with the duplication
  @Operation(summary = "Add the lecture to the course")
    @PostMapping(value = "/courses/{id}/lecture",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?>addLectureToCourse(
          @Valid @ModelAttribute Lecturedto lecture,
          @RequestParam(value = "video",required = false) MultipartFile video,
          @Parameter(example = "68a54f0171e5d5ef4b4fc18c",description = "Id of the course to add the lecture")
          @PathVariable String id

  ){
// The video transcoding is left meaning converting the video in to the different resolutions
try {
    ObjectId objectId=new ObjectId(id);

    CourseEntity course = courseRepository.findById(objectId).orElseThrow(() -> new RuntimeException("Course not found"));
//    Check if the user is valid user
    boolean validUserOfCourse = courseService.isValidUserOfCourse(course);
    if(!validUserOfCourse){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "You are not the valid user of the course"));
    }
    Map<String, String> uploadVideoUrl=null;
    if (video!=null && !video.isEmpty())
    uploadVideoUrl = videoUploadService.uploadVideoWithResolution(video);






//Save the lecture to the course
    LectureReplydto isSaved = courseService.saveLectureInCourse(lecture, course,uploadVideoUrl);
    if (isSaved==null){
        videoUploadService.deleteFile(uploadVideoUrl.get("id"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Lecture is not saved"));
    }


    return ResponseEntity.status(HttpStatus.OK)
            .body(isSaved);

}
catch (IllegalArgumentException e) {
    // ObjectId constructor throws this if id is not a valid 24-char hex string
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", "Invalid course ID format"));
} catch (IOException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "Error adding the video"));
}
    }

    @Operation(summary = "To get all the lecture of a course")
    @GetMapping("/courses/{id}/lecture")
    public ResponseEntity<?>getAllLectureOfCourse(
            @Parameter(example = "68a54f0171e5d5ef4b4fc18c",description = "Id of the course ")
            @PathVariable String id){
        ObjectId  courseId=new ObjectId(id);
        CourseEntity courseByID = courseService.getCourseByID(courseId);
       if (courseByID==null)
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        List<LectureReplydto> lectures = lectureService.getLectures(courseId);
return ResponseEntity.ok(lectures);
    }






//    To add the progress tracker

//   To get Enrollerd Students

}
