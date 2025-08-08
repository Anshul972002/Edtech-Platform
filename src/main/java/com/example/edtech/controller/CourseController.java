package com.example.edtech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Course Api")
@RestController("/course")
public class CourseController {

@GetMapping
    public String hello(){
    return "Hello from course";
}

}
