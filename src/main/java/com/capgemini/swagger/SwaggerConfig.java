package com.capgemini.swagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;
import static com.google.common.base.Predicates.or;
@Profile("swagger-enabled-for-qa")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
				.apiInfo(apiInfo()).select().paths(postPaths()).build();
	}

	/*private Predicate<String> postPaths() {
		return or(regex("/friendmgt/test*"), regex("/friendmgt/test.*"));
	}*/
	
	//return or(regex("/api/posts.*"), regex("/friendmgt/test*"));
	private Predicate<String> postPaths() {
	//	return or(regex("/friendmgt/posts.*"), regex("/friendmgt*"));
		return or(regex("/api/posts.*"), regex("/friendmgt/test*"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("SRS friend mgtm API")
				.description("This is a test")
				.termsOfServiceUrl("http://capgemini.com")
				.contact("abc@gmail.com").license("capgemini License")
				.licenseUrl("abc@gmail.com").version("1.0").build();
	}

}