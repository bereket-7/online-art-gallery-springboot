package com.project.oag.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@ComponentScan(basePackages = { "com.project.oag.security" })
// @ImportResource({ "classpath:webSecurityConfig.xml" })
@Configuration
public class SecSecurityConfig {

	  /*  @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth.inMemoryAuthentication()
	            .withUser("user").password(passwordEncoder().encode("password")).roles("USER");
	    }*/


    //@Autowired
    //private UserDetailsService userDetailsService;

    //@Autowired
    //private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

   // @Autowired
   // private LogoutSuccessHandler myLogoutSuccessHandler;

    //@Autowired
    //private AuthenticationFailureHandler authenticationFailureHandler;

   // @Autowired
    //private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    //@Autowired
    //private UserRepository userRepository;
    
/*

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(authProvider())
            .build();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .authorizeRequests()
                .expressionHandler(webSecurityExpressionHandler())
                .requestMatchers(HttpMethod.GET, "/roleHierarchy")
            //.requestMatchers("/login*", "/logout*", "/signin/**", "/signup/**", "/customLogin", "/user/registration*", "/registrationConfirm*", "/expiredAccount*", "/registration*", "/badUser*", "/user/resendRegistrationToken*", "/forgetPassword*",
                "/user/resetPassword*", "/user/savePassword*", "/updatePassword*", "/user/changePassword*", "/emailError*", "/resources/**", "/old/user/registration*", "/successRegister*", "/qrcode*", "/user/enableNewLoc*")
            .permitAll()
            .requestMatchers("/invalidSession*")
            .anonymous()
            .requestMatchers("/user/updatePassword*")
            .hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
            //.anyRequest()
            .hasAuthority("READ_PRIVILEGE")
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/homepage.html")
            .failureUrl("/login?error=true")
            .successHandler(myAuthenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)
            .authenticationDetailsSource(authenticationDetailsSource)
            .permitAll()
            .and()
            .sessionManagement()
            .invalidSessionUrl("/invalidSession.html")
            .maximumSessions(1)
            .sessionRegistry(sessionRegistry())
            .and()
            .sessionFixation()
            .none()
            .and()
            .logout()
            .logoutSuccessHandler(myLogoutSuccessHandler)
            .invalidateHttpSession(true)
            .logoutSuccessUrl("/logout.html?logSucc=true")
            .deleteCookies("JSESSIONID")
            .permitAll()
            .and()
            .rememberMe()
            .rememberMeServices(rememberMeServices())
            .key("theKey");
        return http.build();
    }*/

    // beans
/*
    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        //authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }*/

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
/*
    @Bean
    public RememberMeServices rememberMeServices() {
        CustomRememberMeServices rememberMeServices = new CustomRememberMeServices("theKey", userDetailsService, new InMemoryTokenRepositoryImpl());
        return rememberMeServices;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }*/

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
