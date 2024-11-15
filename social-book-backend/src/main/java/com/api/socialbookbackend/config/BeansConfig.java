package com.api.socialbookbackend.config;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericParameterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {

    private final UserDetailsService userDetailsService;

    /**
     * This bean is required to be able to inject the AuthenticationProvider in the AuthenticationController
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * This bean is required to be able to inject the AuthenticationManager in the AuthenticationController
     * @param configuration
     * @param parameterBuilder
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration, GenericParameterService parameterBuilder) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * This bean is required to be able to inject the AuditorAware in the ApplicationAuditAware
     * @return
     */
    @Bean
    public AuditorAware<Long> auditorAware() {
        return new ApplicationAuditAware();
    }


    /**
     * This bean is required to be able to inject the PasswordEncoder in the AuthenticationController
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
