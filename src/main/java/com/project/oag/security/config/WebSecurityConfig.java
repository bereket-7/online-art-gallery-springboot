package com.project.oag.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.project.oag.service.UserService;
  
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    private final UserService userService;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//public WebSecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
//		super();
//		this.userService = userService;
//		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//	}

//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    return http.cors()
//            .and().csrf().disable()
//            .authorizeHttpRequests()
//            .requestMatchers("/api/v*/registration/**")
//            .permitAll()
//            .and()
//            .authorizeHttpRequests()
//            .requestMatchers("/users/**")
//            .hasAnyAuthority("USER", "CUSTOMER", "ADMIN", "ARTIST", "MANAGER")
//            .and()
//            .formLogin()
//            .loginPage("/login")
//            .loginProcessingUrl("/authenticate")
//            .and()
//            .build();
//}


//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//  return http.cors()
//           .and().csrf().disable()
//            .authorizeHttpRequests()
//           .requestMatchers("/api/v*/registration/**")
//            .permitAll()
//           .and()
//            .authorizeHttpRequests()
//            .requestMatchers("/users/**")
//            .hasAnyAuthority("USER","CUSTOMER", "ADMIN","ARTIST","MANAGER")
//            .and().formLogin().and().build();
//}

		
//	    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                    .requestMatchers("/api/v*/registration/**")
//                    .permitAll()
//                .anyRequest()
//                .authenticated().and()
//                .formLogin();
//	    }

//	    @Override
//	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	        auth.authenticationProvider(daoAuthenticationProvider());
//	    }

//s
	
//}
