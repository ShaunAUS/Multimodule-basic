package com.example.mindshare.security

import Token
import com.example.mindshare.ErrorCode
import com.example.mindshare.domain.auth.LoginRequest
import com.example.mindshare.domain.auth.UserLoginResponse
import com.example.mindshare.domain.auth.UserRecreateTokensResponse
import com.example.mindshare.error.exception.BusinessException
import com.example.mindshare.error.exception.ForbiddenException
import com.example.mindshare.jwt.JwtTokenProvider
import com.example.mindshare.redis.RedisRepository
import com.example.mindshare.support.findByIdOrThrow
import core.enums.JwtCode
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val redisRepository: RedisRepository,

) : AuthenticationService {

    override fun login(loginRequest: LoginRequest): UserLoginResponse {
        return createTokens(getAuthentication(loginRequest))
    }

    override fun logout(accessToken: String, refreshToken: String) {
        val reFreshTokenById = redisRepository.findById(refreshToken)
        reFreshTokenById.ifPresent {
            redisRepository.delete(reFreshTokenById.get())
        }
        redisRepository.save(Token(token = accessToken, "logout"))
    }

    override fun reCreateBothTokens(accessToken: String, refreshToken: String): UserRecreateTokensResponse {
        checkIsLoginUserBy(refreshToken)
        return when (jwtTokenProvider.validateToken(refreshToken)) {
            JwtCode.ACCESS -> createBothTokens(accessToken)
            JwtCode.EXPIRED -> throw BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN_EXCEPTION)
            JwtCode.DENIED -> throw BusinessException(ErrorCode.DENIED_REFRESH_TOKEN_EXCEPTION)
        }
    }

    private fun createBothTokens(accessToken: String): UserRecreateTokensResponse {
        val authentication = jwtTokenProvider.getAuthentication(accessToken)
        return UserRecreateTokensResponse(
            accessToken = jwtTokenProvider.createAccessToken(authentication),
            refreshToken = jwtTokenProvider.createRefreshToken(authentication),
        )
    }

    // 로그인은 되있어야 재발금 됨
    private fun checkIsLoginUserBy(reFreshToekn: String) {
        redisRepository.findByIdOrThrow(reFreshToekn)
        redisRepository.deleteById(reFreshToekn)
    }

    private fun createTokens(authentication: Authentication): UserLoginResponse {
        return UserLoginResponse(
            accessToken = jwtTokenProvider.createAccessToken(authentication),
            refreshToken = jwtTokenProvider.createRefreshToken(authentication),
        )
    }

    private fun getAuthentication(loginRequest: LoginRequest): Authentication {
        val authentication: Authentication
        try {
            val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.loginId, loginRequest.password)
            authentication = createAuthentication(authenticationToken)
        } catch (e: Exception) {
            throw ForbiddenException(ErrorCode.FORBIDDEN_USER_EXCEPTION)
        }
        return authentication
    }

    private fun createAuthentication(token: UsernamePasswordAuthenticationToken): Authentication {
        return authenticationManagerBuilder.`object`.authenticate(token)
    }
}
