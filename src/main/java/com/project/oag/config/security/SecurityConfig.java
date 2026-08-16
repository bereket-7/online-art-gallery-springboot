package com.project.oag.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.oag.app.service.UserInfoUserDetailsService;
import com.project.oag.app.service.auth.JwtAuthFilter;
import com.project.oag.config.RateLimitFilter;
import com.project.oag.config.properties.SecuritySkipList;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private static final String[] PUBLIC_GET = {
            "/api/v1/artworks",
            "/api/v1/artworks/**",
            "/api/v1/artists/**",
            "/api/v1/collections/**",
            "/api/v1/discovery/**",
            "/api/v1/auctions",
            "/api/v1/auctions/**",
            "/api/v1/events",
            "/api/v1/events/**",
            "/api/v1/competitions",
            "/api/v1/competitions/**",
            "/api/v1/competition/**",
            "/api/v1/certificates/**",
            "/api/v1/ratings/artwork/**",
            "/api/v1/standards",
            "/api/v1/standards/**",
            "/api/v1/cms/config",
            "/api/v1/competitors/winner/**",
            "/ws/notifications/**"
    };

    private final JwtAuthFilter authFilter;
    private final RateLimitFilter rateLimitFilter;
    private final SecuritySkipList securitySkipList;
    private final LogoutHandlerService logoutHandlerService;
    private final CorsOriginConfig corsOriginConfig;
    private final ObjectMapper objectMapper;

    public SecurityConfig(JwtAuthFilter authFilter, RateLimitFilter rateLimitFilter, SecuritySkipList securitySkipList, LogoutHandlerService logoutHandlerService, CorsOriginConfig corsOriginConfig, ObjectMapper objectMapper) {
        this.authFilter = authFilter;
        this.rateLimitFilter = rateLimitFilter;
        this.securitySkipList = securitySkipList;
        this.logoutHandlerService = logoutHandlerService;
        this.corsOriginConfig = corsOriginConfig;
        this.objectMapper = objectMapper;
    }
    @Bean
    public UserDetailsService userDetailsService(UserInfoUserDetailsService userInfoUserDetailsService) {
        return userInfoUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   AuthenticationProvider authenticationProvider) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(securitySkipList.skip().values().stream()
                                        .flatMap(Collection::stream)
                                        .toList().toArray(new String[0]))
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/contact").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(
                        logoutConfigurer -> logoutConfigurer.logoutUrl("/api/v1/auth/logout")
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
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
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
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
