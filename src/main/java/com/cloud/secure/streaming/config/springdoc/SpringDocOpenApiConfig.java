package com.cloud.secure.streaming.config.springdoc;

import com.cloud.secure.streaming.common.utilities.Constant;
import com.cloud.secure.streaming.controllers.ApiPath;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author 689Cloud
 */
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@Configuration
public class SpringDocOpenApiConfig {

    private final String moduleName;
    private final String apiVersion;

    public SpringDocOpenApiConfig(
            @Value("${app.name}") String moduleName,
            @Value("${app.api.version}") String apiVersion) {
        this.moduleName = moduleName;
        this.apiVersion = apiVersion;
    }


    @Bean
    public GroupedOpenApi authenticateApi() {
        return GroupedOpenApi.builder().group("Authenticate").pathsToMatch(ApiPath.AUTHENTICATE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("User").pathsToMatch("/api/user/**").build();
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder().group("Category").pathsToMatch(ApiPath.CATEGORY_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder().group("Product").pathsToMatch(ApiPath.PRODUCT_API + "/**").build();
    }

    @Bean
    public OpenAPI applicationOpenAPI() {
        final String securitySchemeName = Constant.HEADER_TOKEN;
        final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                )
                )
                .info(new Info().title(apiTitle)
                        .version(apiVersion)
                        .license(new License().name("689Cloud Pte.").url("https://www.689cloud.com")));
    }
}
