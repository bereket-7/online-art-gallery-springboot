package com.project.oag.configuration;

//@Configuration
//@ComponentScan(basePackages = { "com.project.oag" })
//@EnableWebMvc
//public class MvcConfig implements WebMvcConfigurer {
//
//    public MvcConfig() {
//        super();
//    }
//
//    @Autowired
//    private MessageSource messageSource;
//
//    @Override
//    public void addViewControllers(final ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:/login");
//        registry.addViewController("/loginRememberMe");
//        registry.addViewController("/customLogin");
//        registry.addViewController("/registration.html");
//        registry.addViewController("/registrationCaptcha.html");
//        registry.addViewController("/registrationReCaptchaV3.html");
//        registry.addViewController("/logout.html");
//        registry.addViewController("/homepage.html");
//        registry.addViewController("/expiredAccount.html");
//        registry.addViewController("/emailError.html");
//        registry.addViewController("/home.html");
//        registry.addViewController("/invalidSession.html");
//        registry.addViewController("/admin.html");
//        registry.addViewController("/successRegister.html");
//        registry.addViewController("/forgetPassword.html");
//        registry.addViewController("/updatePassword.html");
//        registry.addViewController("/changePassword.html");
//        registry.addViewController("/users.html");
//        registry.addViewController("/qrcode.html");
//    }
//
//    @Override
//    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
//
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**").addResourceLocations("/", "/resources/");
//    }
//
//    @Override
//    public void addInterceptors(final InterceptorRegistry registry) {
//        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
//        localeChangeInterceptor.setParamName("lang");
//        registry.addInterceptor(localeChangeInterceptor);
//    }
//
//
//    // beans
//
//    @Bean
//    public LocaleResolver localeResolver() {
//        final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
//        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
//        return cookieLocaleResolver;
//    }
//
//    @Bean
//    public EmailValidator usernameValidator() {
//        return new EmailValidator();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(RequestContextListener.class)
//    public RequestContextListener requestContextListener() {
//        return new RequestContextListener();
//    }
//
//    @Override
//    public Validator getValidator() {
//        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
//        validator.setValidationMessageSource(messageSource);
//        return validator;
//    }
//
//    @Bean
//    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> enableDefaultServlet() {
//        return (factory) -> factory.setRegisterDefaultServlet(true);
//    }
//
//    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
//        Path uploadDir = Paths.get(dirName);
//        String uploadPath = uploadDir.toFile().getAbsolutePath();
//
//        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
//
//        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
//    }
//}
