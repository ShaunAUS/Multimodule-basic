package com.example.mindshare.security

import com.example.mindshare.jwt.JwtAuthenticationFilter
import com.example.mindshare.jwt.JwtTokenProvider
import com.example.mindshare.redis.RedisRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisRepository: RedisRepository,
    private val entryPoint: AuthenticationEntryPoint,
    @Value("\${jwt.headers.authorization}")
    private val authorizationHeader: String,

    @Value("\${jwt.headers.refresh}")
    private val refreshHeader: String,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
/*
            .authorizeHttpRequests {
                it.requestMatchers("/api/v1/member/test").hasRole("ADMIN")
                    .anyRequest().permitAll()
            }
*/
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider, redisRepository, authorizationHeader, refreshHeader),
                UsernamePasswordAuthenticationFilter::class.java,
            )
            .exceptionHandling { it.authenticationEntryPoint(entryPoint) }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}
