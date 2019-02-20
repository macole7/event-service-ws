package com.service.eventservice.service;

import com.service.eventservice.exception.UserNotFoundException;
import com.service.eventservice.model.User;
import com.service.eventservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found " + id));
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found " + id));
        userRepository.deleteById(id);
        return user;
    }

    @Override
    public User updateUser(User user, long id) {
        User userById = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found " + id));
        userById.setUsername(user.getUsername());
        userById.setEmail(user.getEmail());
        userRepository.save(userById);
        return userById;
    }

    @Override
    public List<User> findByUsername(String username) {
        List<User> usersByUsername = userRepository.findByUsername(username);
        if (usersByUsername.isEmpty()) {
            throw new UserNotFoundException("User " + username + " does not exist");
        }
        return usersByUsername;
    }
}
