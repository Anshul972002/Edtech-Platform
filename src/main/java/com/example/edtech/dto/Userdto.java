package com.example.edtech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Userdto {

    private String id;
    @NotBlank(message = "Name connot be empty")
    private String name;
    @NotBlank(message = "Please fill the dob")
    private String dob;


    @NotBlank(message = "Enter the address")
    private String address;
  @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8,message = "Password must be 8 characters long")
    @Pattern(
            regexp ="^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message ="Password must contain at least one uppercase,one lowercase,one digit and one special charaters"
    )
    private String password;
    @NotBlank(message = "Please enter the password")
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