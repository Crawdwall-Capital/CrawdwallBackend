package com.crawdwall_backend_api.utils.appsecurity;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@SecurityScheme(
        name = "ApiKeyAuth",
        type = SecuritySchemeType.APIKEY,
        paramName = "X-API-KEY",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenAPIConfig {

   @Bean
    public OpenAPI serviceAPI() {
        return new OpenAPI()
                .info(new Info().title("Crawdwall Portal API")
                        .description("REST API for Crawdwall Portal application")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/Crawdwall/Crawdwall-portal-api"));
    }
}