package com.example.edtech.util;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUser {
 @NotBlank(message = "Enter the email")
    private String email;
 @NotBlank(message = "Enter the password")
 private String password;
}
