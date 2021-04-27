package com.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket doc() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
	}

	public ApiInfo apiInfo() {
		return new ApiInfo("AssignmentTimeSheet", "Twendee Training Project", "0.0.1", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "Diep, Bao", "Application License", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
	}
}
