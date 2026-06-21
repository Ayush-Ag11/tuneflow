package com.tuneflow.user_service.service;

import com.tuneflow.user_service.dto.request.CreateUserProfileRequest;
import com.tuneflow.user_service.dto.request.UpdateUserProfileRequest;
import com.tuneflow.user_service.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse createUserProfile(CreateUserProfileRequest request);

    UserResponse getUserById(UUID userId);

    UserResponse updateUserProfile(UUID userId, UpdateUserProfileRequest request);

    void deactivateUser(UUID userId);
}
