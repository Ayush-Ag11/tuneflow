package com.tuneflow.gateway_service.filter;

import com.tuneflow.gateway_service.security.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    // Paths that bypass JWT validation entirely
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/verify-email",
            "/api/v1/auth/resend-verification",
            "/api/v1/auth/refresh-token",
            "/api/v1/artists/**",
            "/api/v1/artists",
            "/actuator/health",
            "/api/v1/genres/**",
            "/api/v1/genres"
    );
    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        if (!jwtUtils.isTokenValid(token)) {
            return unauthorized(exchange);
        }

        // Token is valid — extract claims and forward user identity downstream
        Claims claims = jwtUtils.extractAllClaims(token);

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(r -> r
                        .header("X-User-Id", claims.getSubject())
                        .header("X-User-Email",
                                claims.get("email", String.class))
                        .header("X-User-Name",
                                claims.get("username", String.class))
                        // Strip original Authorization header so downstream
                        // services never see the raw JWT
                        .headers(headers ->
                                headers.remove(HttpHeaders.AUTHORIZATION))
                )
                .build();

        log.debug("Authenticated request from userId={} path={}",
                claims.getSubject(), path);

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        // Run before all other filters
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream()
                .anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}