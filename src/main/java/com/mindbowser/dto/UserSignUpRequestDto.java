package com.mindbowser.dto;


import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserSignUpRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Email should not be empty.")
    @Email(message = "Please provide valid email address.")
    private String email;

    @NotNull(message = "Firstname should not be empty.")
    private String firstname;

    @NotNull(message = "Password should not be empty.")
    @Size(min = 6, max = 15, message="Password should be between 6 to 15 characters.")
    private String password;

    @NotNull(message = "Lastname should not be empty.")
    private String lastname;

    @NotNull(message = "Date should not be empty.")
    private String birthDate;

    @NotNull(message = "Address should not be empty.")
    private String address;

    @NotNull(message = "Company name should not be empty.")
    private String companyName;
}
