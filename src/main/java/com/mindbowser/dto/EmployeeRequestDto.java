package com.mindbowser.dto;

import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployeeRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Firstname should not be empty")
    private String firstname;

    @NotNull(message = "Lastname should not be empty")
    private String lastname;

    @NotNull(message = "Address should not be empty")
    private String address;

    @NotNull(message = "Birth date should not be empty.")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @NotNull(message = "Mobile should not be empty.")
    @Pattern(regexp="(^$|[0-9]{10})", message="Please enter correct mobile number.")
    private String mobile;

    @NotNull(message = "City should not be empty.")
    private String city;
}
