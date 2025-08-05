package com.example.edtech.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Userdto {
    @NotBlank(message = "Name cannot be empty")
    @Schema(example = "Anshul Yadav", description = "Full name of the user")
    private String name;

    @NotBlank(message = "Please fill the dob")
    @Schema(example = "09/07/2003", description = "Date of birth in DD/MM/YYYY format")
    private String dob;

    @NotBlank(message = "Enter the address")
    @Schema(example = "Ghazipur", description = "Home address of the user")
    private String address;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @Schema(example = "anshul@2002.com", description = "Valid email address")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be 8 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase, one lowercase, one digit and one special character"
    )
    @Schema(example = "Anshul@123", description = "Secure password meeting required criteria")
    private String password;

    @NotBlank(message = "Please confirm the password")
    @Schema(example = "Anshul@123", description = "Must match the password")
    private String confirmpassword;
}

//
//{
//  "name": "Ansul",
//  "dob": "2002/09/07",
//  "address": "Ghazipur",
//  "email": "anshul@2002",
//  "password": "Anshul123",
//  "confirmpassword": "Anshul123",
//
//}