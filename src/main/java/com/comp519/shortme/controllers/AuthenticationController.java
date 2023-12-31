package com.comp519.shortme.controllers;

import com.comp519.shortme.configurations.UserAuthProvider;
import com.comp519.shortme.dto.UserLoginRequestDto;
import com.comp519.shortme.dto.UserRegisterRequestDto;
import com.comp519.shortme.dto.UserResponseDto;
import com.comp519.shortme.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @Autowired
    public AuthenticationController(UserService userService, UserAuthProvider userAuthProvider) {
        this.userService = userService;
        this.userAuthProvider = userAuthProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRegisterRequestDto registrationDto) throws Exception {
        UserResponseDto userResponseDto = userService.registerUser(registrationDto);
        userResponseDto.setToken(userAuthProvider.createToken(userResponseDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> loginUser(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) throws Exception {
        UserResponseDto userResponseDto = userService.loginUser(userLoginRequestDto);
        userResponseDto.setToken(userAuthProvider.createToken(userResponseDto));

        return ResponseEntity.ok(userResponseDto);
    }
}
