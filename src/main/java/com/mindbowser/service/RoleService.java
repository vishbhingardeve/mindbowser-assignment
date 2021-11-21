package com.mindbowser.service;


import com.mindbowser.entity.Role;

public interface RoleService {
    Role findByName(String name);
}
