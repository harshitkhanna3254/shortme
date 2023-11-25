package com.comp519.shortme.controllers;

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

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterRequestDto registrationDto) throws Exception {
        userService.registerUser(registrationDto);

//        UserResponseDto userResponseDto = new UserResponseDto(registrationDto.getUsername(), REGISTER_SUCCESSFUL_MESSAGE);
        return ResponseEntity.ok("User Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> loginUser(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) throws Exception {
        UserResponseDto userResponseDto = userService.loginUser(userLoginRequestDto);

        return ResponseEntity.ok(userResponseDto);
    }
}
