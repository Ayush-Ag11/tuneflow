package com.tuneflow.user_service.service.impl;

import com.tuneflow.user_service.dto.request.CreateUserProfileRequest;
import com.tuneflow.user_service.dto.request.UpdateUserProfileRequest;
import com.tuneflow.user_service.dto.response.UserResponse;
import com.tuneflow.user_service.entity.User;
import com.tuneflow.user_service.exception.DuplicateResourceException;
import com.tuneflow.user_service.exception.ResourceNotFoundException;
import com.tuneflow.user_service.mapper.UserMapper;
import com.tuneflow.user_service.repository.UserRepository;
import com.tuneflow.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUserProfile(
            CreateUserProfileRequest request) {

        if (userRepository.existsById(request.id())) {
            throw new DuplicateResourceException(
                    "User profile already exists"
            );
        }

        User user = User.builder()
                .id(request.id())
                .username(request.username())
                .email(request.email())
                .active(true)
                .build();

        User savedUser =
                userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(UUID userId) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUserProfile(UUID userId, UpdateUserProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }

        if (request.bio() != null) {
            user.setBio(request.bio());
        }

        if (request.profileImageUrl() != null) {
            user.setProfileImageUrl(request.profileImageUrl());
        }

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public void deactivateUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setActive(false);

        userRepository.save(user);
    }
}
