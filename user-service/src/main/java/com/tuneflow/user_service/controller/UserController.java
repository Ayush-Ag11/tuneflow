package com.tuneflow.user_service.controller;

import com.tuneflow.user_service.dto.request.UpdateUserProfileRequest;
import com.tuneflow.user_service.dto.response.ApiResponse;
import com.tuneflow.user_service.dto.response.UserResponse;
import com.tuneflow.user_service.security.UserContext;
import com.tuneflow.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserContext userContext;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable UUID userId) {

        UserResponse response = userService.getUserById(userId);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "User fetched successfully",
                response
        ));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable UUID userId,
                                                                @Valid @RequestBody UpdateUserProfileRequest request) {
        UserResponse response = userService.updateUserProfile(userId, request);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "User updated successfully",
                response)
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable UUID userId) {

        userService.deactivateUser(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "User deactivated successfully",
                        null
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {

        UUID userId = userContext.getCurrentUserId();

        UserResponse response =
                userService.getUserById(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Current user fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
            @Valid @RequestBody UpdateUserProfileRequest request) {

        UUID userId = userContext.getCurrentUserId();

        UserResponse response =
                userService.updateUserProfile(
                        userId,
                        request
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "User updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser() {

        UUID userId = userContext.getCurrentUserId();

        userService.deactivateUser(userId);

        return ResponseEntity.noContent()
                .build();
    }
}
