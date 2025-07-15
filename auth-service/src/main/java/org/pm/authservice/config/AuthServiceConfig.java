package org.pm.authservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@Slf4j
public class AuthServiceConfig {

    @Bean
    public Clock clock() {
        Clock clock = Clock.systemUTC();
        log.info("Clock set to UTC");
        log.info("Clock set to {}", clock.instant());
        return clock; // or Clock.systemDefaultZone()
    }
}