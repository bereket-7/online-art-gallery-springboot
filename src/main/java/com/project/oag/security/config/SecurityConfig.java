package com.project.oag.security.config;
import com.project.oag.security.filter.JwtTokenFilter;

import com.project.oag.security.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtTokenFilter jwtAuthenticationFilter;
    //private final UserDetailsService userDetailsService;
    private final CustomUserDetailsService customUserDetailsService;
    public SecurityConfig(JwtTokenFilter jwtAuthenticationFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.customUserDetailsService.setPasswordEncoder(bCryptPasswordEncoder());
    }

//    public SecurityConfig(JwtTokenFilter jwtAuthenticationFilter,
//                          UserDetailsService userDetailsService) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//        this.userDetailsService = userDetailsService;
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.headers().frameOptions().disable();

        httpSecurity.cors().and().csrf().disable();
        //@formatter:off
        httpSecurity
                .authorizeHttpRequests()
                //.requestMatchers( "/api/v1/registration/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/registration/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/registration/confirm").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException)
                                -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                authException.getLocalizedMessage()
                        )
                )
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //@formatter:on
        return httpSecurity.build();
    }
}