package com.example.edtech.controller;

import com.example.edtech.dto.Coursedto;
import com.example.edtech.entity.CourseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Teacher api",description = "Endpoint for the teacher related api")
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping
    public String hello(){
        return "Hello from the teachers";
    }

    @Operation(description = "Create new Course")
    @PostMapping("/courses")
    public ResponseEntity<Map<String,Object>>courses(@Valid @RequestBody Coursedto course){

        return ResponseEntity.ok(Map.of("message","Sucessfull"));
    }

}
