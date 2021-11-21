package com.mindbowser.service.impl;

import com.mindbowser.repository.UserDao;
import com.mindbowser.dto.UserSignUpRequestDto;
import com.mindbowser.entity.Role;
import com.mindbowser.entity.User;
import com.mindbowser.service.RoleService;
import com.mindbowser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        return authorities;
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public User findOne(String username) {
        return userDao.findByEmail(username);
    }

    @Override
    public User save(UserSignUpRequestDto user) {
        User userSave = new User();
        userSave.setEmail(user.getEmail());
        userSave.setPassword(user.getPassword());
        userSave.setFirstname(user.getFirstname());
        userSave.setLastname(user.getLastname());
        userSave.setBirthDate(user.getBirthDate());
        userSave.setAddress(user.getAddress());
        userSave.setCompanyName(user.getCompanyName());
        userSave.setPassword(bcryptEncoder.encode(user.getPassword()));
        Role role = roleService.findByName("MANAGER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        userSave.setRoles(roleSet);
        return userDao.save(userSave);
    }
}
