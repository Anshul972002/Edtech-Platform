package com.example.edtech.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Api",description = "Endpoint for user-related operations")
@RestController
@RequestMapping("user")
public class UserController {
@SecurityRequirement(name = "bearerAuth")
    @GetMapping("/hello")
    public String hello(){
    return "Hi";
    }
}

// profile management

// forgot password

//course module

