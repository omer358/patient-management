package org.pm.authservice.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AuthServiceConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC(); // or Clock.systemDefaultZone()
    }
}