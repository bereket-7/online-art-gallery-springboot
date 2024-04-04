package com.project.oag.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {
        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2).select()
                    .apis(RequestHandlerSelectors.basePackage("com.project.oag.app.controller"))
                    .paths(PathSelectors.regex("/.*"))
                    .build().apiInfo(apiInfoMetaData());
        }

    private ApiInfo apiInfoMetaData() {
            return new ApiInfoBuilder().title("OAG")
                    .description("Online Art Gallery")
                    .termsOfServiceUrl("http://localhost:8088/terms")
                    .version("1.0.0")
                    .license("Apache License Version 2.0")
                    .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                    .build();

    }
}
