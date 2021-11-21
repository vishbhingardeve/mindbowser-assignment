package com.mindbowser.service;

import com.mindbowser.dto.UserSignUpRequestDto;
import com.mindbowser.entity.User;

import java.util.List;

public interface UserService {
    User save(UserSignUpRequestDto user);
    List<User> findAll();
    User findOne(String username);
}
