package com.project.oag.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.oag.app.service.UserInfoUserDetailsService;
import com.project.oag.app.service.auth.JwtAuthFilter;
import com.project.oag.config.properties.SecuritySkipList;
import com.project.oag.config.security.captcha.RecaptchaFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static com.project.oag.utils.Utils.prepareResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthFilter authFilter;
    private final RecaptchaFilter recaptchaFilter;
    private final SecuritySkipList securitySkipList;
    private final LogoutHandlerService logoutHandlerService;
    private final CorsOriginConfig corsOriginConfig;
    private final ObjectMapper objectMapper;

    public SecurityConfig(JwtAuthFilter authFilter, RecaptchaFilter recaptchaFilter, SecuritySkipList securitySkipList, LogoutHandlerService logoutHandlerService, CorsOriginConfig corsOriginConfig, ObjectMapper objectMapper) {
        this.authFilter = authFilter;
        this.recaptchaFilter = recaptchaFilter;
        this.securitySkipList = securitySkipList;
        this.logoutHandlerService = logoutHandlerService;
        this.corsOriginConfig = corsOriginConfig;
        this.objectMapper = objectMapper;
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(securitySkipList.skip().values().stream()
                                        .flatMap(Collection::stream)
                                        .toList().toArray(new String[0]))
                                .permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(
                        logoutConfigurer -> logoutConfigurer.logoutUrl("/api/v1/logout")
                                .addLogoutHandler(logoutHandlerService)
                                .logoutSuccessHandler((request, response, authentication) -> logoutSuccess(response))
                )
                .build();
    }
    private void logoutSuccess(HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(prepareResponse(HttpStatus.OK, "Logout successful", Collections.emptyList())));
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsOriginConfig.listOfOrigins());
        configuration.setAllowedMethods(corsOriginConfig.allowedMethods());
        configuration.setAllowedHeaders(corsOriginConfig.allowedHeaders());
        configuration.setExposedHeaders(corsOriginConfig.exposedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
