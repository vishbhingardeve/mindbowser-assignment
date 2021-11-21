package com.mindbowser.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String address;

    @Temporal(TemporalType.DATE)
    @Column
    private Date birthDate;

    @Column
    private String mobile;

    @Column
    private String city;

}
