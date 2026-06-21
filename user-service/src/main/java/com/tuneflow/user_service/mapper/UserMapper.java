package com.tuneflow.user_service.mapper;

import com.tuneflow.user_service.dto.response.UserResponse;
import com.tuneflow.user_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}