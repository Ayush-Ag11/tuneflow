package com.tuneflow.user_service.controller;

import com.tuneflow.user_service.dto.request.CreateUserProfileRequest;
import com.tuneflow.user_service.dto.response.UserResponse;
import com.tuneflow.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserProfileRequest request) {

        UserResponse response = userService.createUserProfile(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}