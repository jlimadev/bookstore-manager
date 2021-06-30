package com.jlima.bookstoremanager.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    companion object {
        private const val BASE_PACKAGE = "com.jlima.bookstoremanager"
        private const val API_TITLE = "Bookstore Manager API"
        private const val API_DESCRIPTION = "Bookstore Manager Project"
        private const val API_VERSION = "1.0.0"
        private const val CONTACT_NAME = "Jonathan Lima"
        private const val CONTACT_EMAIL = "https://github.com/jlimadev"
        private const val CONTACT_URL = "jlima.dev@gmail.com"
    }

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(buildApiInfo())
    }

    private fun buildApiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title(API_TITLE)
            .description(API_DESCRIPTION)
            .version(API_VERSION)
            .contact(Contact(CONTACT_NAME, CONTACT_EMAIL, CONTACT_URL))
            .build()
    }
}