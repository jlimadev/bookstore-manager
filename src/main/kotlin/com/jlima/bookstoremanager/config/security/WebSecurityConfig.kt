package com.jlima.bookstoremanager.config.security

import com.jlima.bookstoremanager.enums.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    private val jwtAuthenticationEntrypoint: JwtAuthenticationEntrypoint,
) : WebSecurityConfigurerAdapter() {
    companion object {
        private val ROLE_ADMIN = Role.ADMIN.name
        private val ROLE_USER = Role.USER.name

        private val AUTH_WHITELIST = arrayOf(
            "/actuator/**",
            "/springfox/**",
            "/springdoc/**",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-resources/**",
            "/webjars/**",
            "/authenticate"
        )

        private val ONLY_ADMIN = arrayOf(
            "/authors/**",
            "/publishers/**",
        )

        private val ALL_AUTHENTICATED = arrayOf(
            "/books/**",
        )
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
            .authorizeRequests()
            .antMatchers(*AUTH_WHITELIST).permitAll()
            .antMatchers(*ONLY_ADMIN).hasAnyRole(ROLE_ADMIN)
            .antMatchers(*ALL_AUTHENTICATED).hasAnyRole(ROLE_ADMIN, ROLE_USER)
            .anyRequest().authenticated()

        httpSecurity
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntrypoint)

        httpSecurity
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        httpSecurity
            .csrf().disable()
            .headers().frameOptions().disable()
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(*AUTH_WHITELIST)
    }
}
