package org.example.authservice.controller;

import org.example.authservice.model.AuthenticateUserDTO;
import org.example.authservice.model.JwkSetResponseDTO;
import org.example.authservice.model.JwtResponseDTO;
import org.example.authservice.model.RegisterUserDTO;
import org.example.authservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/jwks")
    public JwkSetResponseDTO getJwks() {
        return userService.getJwks();
    }

    @PostMapping("/authenticate")
    public JwtResponseDTO authenticate(@RequestBody AuthenticateUserDTO authenticateUserDTO) {
        return userService.authenticate(authenticateUserDTO.getEmail(), authenticateUserDTO.getPassword());
    }

    @PostMapping("/register")
    public JwtResponseDTO register(@RequestBody RegisterUserDTO registerUserDTO) {
        return userService.register(registerUserDTO);
    }
}
