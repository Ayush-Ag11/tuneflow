package com.tuneflow.auth_service.client;

import com.tuneflow.auth_service.dto.request.CreateUserProfileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service",
        url = "${services.user-service.url}"
)
public interface UserServiceClient {

    @PostMapping("/internal/users")
    void createUserProfile(
            @RequestBody CreateUserProfileRequest request
    );
}
