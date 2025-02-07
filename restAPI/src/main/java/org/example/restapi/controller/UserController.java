package org.example.restapi.controller;

import org.example.restapi.model.UserModel;
import org.example.restapi.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserModel getCurrentUser(Authentication authentication) {
        return userService.getUser(authentication.getName());
    }

    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public UserModel getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }
}
