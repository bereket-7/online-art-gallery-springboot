package com.project.oag.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.oag.service.UserService;
  
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

public WebSecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

//		@Override
//	    protected void configure(HttpSecurity http) throws Exception {
//	        http
//	                .csrf().disable()
//	                .authorizeRequests()
//	                    .antMatchers("/api/v*/registration/**")
//	                    .permitAll()
//	                .anyRequest()
//	                .authenticated().and()
//	                .formLogin();
//	    }

//	    @Override
//	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	        auth.authenticationProvider(daoAuthenticationProvider());
//	    }

	    @Bean
	    public DaoAuthenticationProvider daoAuthenticationProvider() {
	        DaoAuthenticationProvider provider =
	                new DaoAuthenticationProvider();
	        provider.setPasswordEncoder(bCryptPasswordEncoder);
	        provider.setUserDetailsService(userService);
	        return provider;
	    }
	
}
