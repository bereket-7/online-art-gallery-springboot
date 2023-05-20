package com.project.oag.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomConfig {
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowedOrigins("*");
			}
			
			  @Override
		        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		            configurer.defaultContentType(MediaType.APPLICATION_JSON);
		        }
		};
	}

}
