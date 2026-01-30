package com.crawdwall_backend_api.utils.appsecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final ApiSecurityFilter apiSecurityFilter;
    private final RestAuthEntryPoint authEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(ApiSecurityFilter apiSecurityFilter,
                          RestAuthEntryPoint authEntryPoint,
                          RestAccessDeniedHandler accessDeniedHandler) {
        log.info("SecurityConfig constructor initialized");
        this.apiSecurityFilter = apiSecurityFilter;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Initializing SecurityFilterChain");
        http
                .csrf(csrf -> {
                    csrf.disable();
                    log.debug("CSRF disabled");
                })
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                    log.debug("CORS configuration source set");
                })
                .sessionManagement(sm -> {
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    log.debug("Session management set to stateless");
                })
                .exceptionHandling(eh -> {
                    eh.authenticationEntryPoint(authEntryPoint)
                            .accessDeniedHandler(accessDeniedHandler);
                    log.debug("Exception handlers configured");
                })
                .authorizeHttpRequests(auth -> {
                    log.debug("Setting up authorization rules");
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers(

                                    "/magik/public/**",
                                    "/api/v1/utilities/public/**",
                                    "/api/v1/utilities/public",
                                    "/swagger-ui.html",
                                    "/swagger-ui/**",
                                    "/api-docs/**",
                                    "/v3/api-docs/**"
                            ).permitAll()
                            .anyRequest().authenticated();
                    log.info("Authorization rules set");
                })
                .addFilterBefore(apiSecurityFilter, UsernamePasswordAuthenticationFilter.class);
        log.info("ApiSecurityFilter added before UsernamePasswordAuthenticationFilter");
        SecurityFilterChain chain = http.build();
        log.info("SecurityFilterChain created successfully");
        return chain;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        log.debug("AuthenticationManager bean created");
        return cfg.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.debug("Configuring CORS");
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(Arrays.asList("*"));
        c.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        c.setAllowedHeaders(Arrays.asList("*"));
        c.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", c);
        log.info("CORS configuration source ready");
        return src;
    }
}
