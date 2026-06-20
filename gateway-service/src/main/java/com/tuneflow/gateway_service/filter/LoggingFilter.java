package com.tuneflow.gateway_service.filter;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingFilter {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingFilter.class);

    @PostConstruct
    public void init() {
        log.info("LoggingFilter Loaded");
    }

    @Bean
    public GlobalFilter customGlobalFilter() {

        return (exchange, chain) -> {
            log.info("Incoming Request => {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
            return chain.filter(exchange);
        };
    }
}
