package ua.foxminded.herasimov.warehouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiDetails())
            .select()
            .apis(RequestHandlerSelectors.basePackage("ua.foxminded.herasimov.warehouse.controller"))
            .build();
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(
            "Warehouse API",
            "API for managing warehouse processes",
            "0.1.3",
            "Free to use",
            new springfox.documentation.service.Contact("Vlad Herasimov", "https://swagger.io", "vlad9880@gmail.com"),
            "Warehouse License 2022",
            "https://swagger.io",
            Collections.emptyList());
    }
}
