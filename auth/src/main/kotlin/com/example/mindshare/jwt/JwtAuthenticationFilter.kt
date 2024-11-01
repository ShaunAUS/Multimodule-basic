package com.example.mindshare.jwt

import com.example.mindshare.ErrorCode
import com.example.mindshare.error.exception.BusinessException
import com.example.mindshare.error.exception.JwtExpiredException
import com.example.mindshare.error.exception.RedisLogOutException
import com.example.mindshare.redis.RedisRepository
import core.enums.JwtCode
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

// FIXME 접근 권한이 없는 경우 AccessDeniedException,  근데 이걸로 한번에 잡아버리면 JWTAuthenticatioFilter에서 발생한 예외가 다 일로감
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisRepository: RedisRepository,
    private val authorizationHeader: String,
    private val refreshHeader: String,
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val refreshToken = resolveToken(request as HttpServletRequest, refreshHeader)
        val accessToken = resolveToken(request, authorizationHeader)

        if (onlyAccessTokenCome(accessToken, refreshToken)) {
            try {
                checkFromRedis(accessToken!!)
                tokenValidation(accessToken)
            } catch (e: RedisLogOutException) {
                request.setAttribute("exception", e)
            } catch (e: Exception) {
                request.setAttribute("exception", e)
            }
        } else if (accessToken == null && refreshToken != null) {
            request.setAttribute("exception", BusinessException(ErrorCode.ONLY_REFRESH_TOKEN_EXCEPTION))
        }

        chain?.doFilter(request, response)
    }

    private fun tokenValidation(accessToken: String) {
        val jwtCode = jwtTokenProvider.validateToken(accessToken)
        when (jwtCode) {
            JwtCode.ACCESS -> setSecurityContextHolder(accessToken)
            JwtCode.EXPIRED -> throw JwtExpiredException(ErrorCode.EXPIRED_JWT_TOKEN_EXCEPTION)
            else -> throw JwtException("jwt denied")
        }
    }

    private fun onlyAccessTokenCome(accessToken: String?, refreshToken: String?) = accessToken != null && refreshToken == null

    // AccessToken이 Redis에 있으면 로그아웃된 유저로 간주
    private fun checkFromRedis(accessToken: String) {
        val redisResult = redisRepository.findByToken(accessToken)
        redisResult?.let {
            if ("logout" == it.value) {
                throw RedisLogOutException(ErrorCode.LOGOUT_USER_ACCESS_TOKEN_EXCPETION)
            }
        }
    }

    private fun setSecurityContextHolder(accessToken: String) {
        val authentication = jwtTokenProvider.getAuthentication(accessToken)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun resolveToken(request: HttpServletRequest, header: String): String? {
        val bearerToken = request.getHeader(header)
        return if (header == authorizationHeader && bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            bearerToken
        }
    }
}
