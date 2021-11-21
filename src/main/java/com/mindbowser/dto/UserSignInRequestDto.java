package com.mindbowser.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserSignInRequestDto {

    @NotNull(message = "Email should not be empty.")
    @Email(message = "Please provide valid email address.")
    private String email;
    @NotNull(message = "Password should not be empty.")
    private String password;
}
