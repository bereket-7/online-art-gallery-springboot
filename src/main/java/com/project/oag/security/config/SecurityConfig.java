package com.project.oag.security.config;
import com.project.oag.security.filter.JwtTokenFilter;
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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtTokenFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtTokenFilter jwtAuthenticationFilter,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.headers().frameOptions().disable();

        httpSecurity.cors().and().csrf().disable();

        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/v1/registration/register").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users/password/reset").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/registration/confirm").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artworks/category/{category}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artworks/priceRange").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artworks/{id}/image").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artworks/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artworks").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/artworks/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artworks/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artworks/autocomplete").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artworks/recent").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artworks/accepted").permitAll()
                .requestMatchers(HttpMethod.POST,"/rating/artworks/{artworkId}/rate").permitAll()
                .requestMatchers(HttpMethod.GET,"/rating/artworks/{artworkId}/average-rating").permitAll()
                .requestMatchers(HttpMethod.POST,"/email/sendEmail").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(
                                        HttpServletResponse.SC_UNAUTHORIZED,
                                        authException.getLocalizedMessage()
                                )
                )
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
