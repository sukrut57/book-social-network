package com.api.socialbookbackend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig{

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Configures the security filter chain that carries out authentication and authorization.
     * @param httpSecurity the HttpSecurity object to configure
     * @return the SecurityFilterChain object that carries out authentication and authorization
     * @throws Exception if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       return httpSecurity.cors(Customizer.withDefaults())
               .csrf(AbstractHttpConfigurer:: disable)
               .authorizeHttpRequests(requests ->
                       requests.requestMatchers(
                                       "/auth/**",
                                       "/v2/api-docs/**",
                                       "/v3/api-docs",
                                       "/v2/api-docs/**",
                                       "/swagger-resources",
                                       "/swagger-resources/**",
                                       "/configuration/ui",
                                       "/configuration/security",
                                       "/swagger-ui/**",
                                       "/webjars/**",
                                       "/swagger-ui.html"
                               ).permitAll()
                               .anyRequest().authenticated()
               )
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authenticationProvider(authenticationProvider)
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
    }

}
