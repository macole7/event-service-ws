package com.service.eventservice.controller;

import com.service.eventservice.model.User;
import com.service.eventservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping(path = "/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @GetMapping("/username")
    public List<User> getUserByUsername(@RequestParam(value = "username") String username) {
        return userService.findByUsername(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Validated User user) {
        return userService.createUser(user);
    }

    @DeleteMapping(path = "/{id}")
    public User deleteUserById(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public User updateUserById(@RequestBody User user, @PathVariable int id) {
        return userService.updateUser(user, id);
    }
}
