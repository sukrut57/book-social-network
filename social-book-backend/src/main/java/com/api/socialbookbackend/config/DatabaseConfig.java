package com.api.socialbookbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
          "com.api.socialbookbackend.book"
        , "com.api.socialbookbackend.user"
        , "com.api.socialbookbackend.role"
        , "com.api.socialbookbackend.feedback",
          "com.api.socialbookbackend.history",
        })
public class DatabaseConfig {
}
