package com.service.eventservice.service;

import com.service.eventservice.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User findById(long id);

    User createUser(User user);

    User deleteUser(long id);

    User updateUser(User user, long id);

    List<User> findByUsername(String username);
}
