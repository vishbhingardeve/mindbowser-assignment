package com.mindbowser.dto;

import lombok.Data;

@Data
public class UserResponseDto {

    private String email;
    private String firstname;
    private String password;
    private String lastname;
    private String birthDate;
    private String address;
    private String companyName;
}
