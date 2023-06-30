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
    public GroupedOpenApi teachingExperiencesApi() {
        return GroupedOpenApi.builder().group("TeachingExperience").pathsToMatch(ApiPath.TEACHING_EXPERIENCE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi jobExperiencesApi() {
        return GroupedOpenApi.builder().group("JobExperience").pathsToMatch(ApiPath.JOB_EXPERIENCE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi bankInformationApi() {
        return GroupedOpenApi.builder().group("BankInformation").pathsToMatch(ApiPath.BANK_INFORMATION_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi announcementApi() {
        return GroupedOpenApi.builder().group("Announcement").pathsToMatch(ApiPath.ANNOUNCEMENT_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi commentApi() {
        return GroupedOpenApi.builder().group("Comment").pathsToMatch(ApiPath.COMMENT_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder().group("Category").pathsToMatch(ApiPath.CATEGORY_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi courseApi() {
        return GroupedOpenApi.builder().group("Course").pathsToMatch(ApiPath.COURSE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi sectionApi() {
        return GroupedOpenApi.builder().group("Section").pathsToMatch(ApiPath.SECTION_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi intendedLearnerApi() {
        return GroupedOpenApi.builder().group("IntendedLearner").pathsToMatch(ApiPath.INTENDED_LEARNER_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi resourceApi() {
        return GroupedOpenApi.builder().group("Resource").pathsToMatch(ApiPath.RESOURCE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi lectureApi() {
        return GroupedOpenApi.builder().group("Lecture").pathsToMatch(ApiPath.LECTURE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi uploadApi() {
        return GroupedOpenApi.builder().group("Upload").pathsToMatch(ApiPath.UPLOAD_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi certificateApi() {
        return GroupedOpenApi.builder().group("Certificate").pathsToMatch(ApiPath.CERTIFICATE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi cardApi() {
        return GroupedOpenApi.builder().group("Card").pathsToMatch(ApiPath.CARD_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi pointPackageApi() {
        return GroupedOpenApi.builder().group("PointPackage").pathsToMatch(ApiPath.POINT_PACKAGE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi courseOrderApi() {
        return GroupedOpenApi.builder().group("Order").pathsToMatch(ApiPath.ORDER_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi rate() {
        return GroupedOpenApi.builder().group("Rate").pathsToMatch(ApiPath.RATE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi exchange() {
        return GroupedOpenApi.builder().group("Exchange").pathsToMatch(ApiPath.EXCHANGE_RATE_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi mediaInfo() {
        return GroupedOpenApi.builder().group("MediaInfo").pathsToMatch(ApiPath.MEDIA_INFO_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi transaction() {
        return GroupedOpenApi.builder().group("Transaction").pathsToMatch(ApiPath.TRANSACTION_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi courseReview() {
        return GroupedOpenApi.builder().group("CourseReview").pathsToMatch(ApiPath.COURSE_REVIEW_API + "/**").build();
    }

    @Bean
    public GroupedOpenApi courseReport() {
        return GroupedOpenApi.builder().group("CourseReport").pathsToMatch(ApiPath.COURSE_REPORT_API + "/**").build();
    }
    @Bean
    public GroupedOpenApi pointHistory() {
        return GroupedOpenApi.builder().group("PointHistory").pathsToMatch(ApiPath.POINT_HISTORY_API + "/**").build();
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
