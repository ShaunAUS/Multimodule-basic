package com.example.mindshare.jwt

import Token
import com.example.mindshare.ErrorCode
import com.example.mindshare.error.exception.JwtClaimException
import com.example.mindshare.redis.RedisRepository
import core.enums.JwtCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider(
    private val redisRepository: RedisRepository,

    @Value("\${jwt.secret}")
    private val secretKey: String,
    @Value("\${jwt.access-token-expiration-time}")
    private val accessTokenExpirationTime: Long,
    @Value("\${jwt.refresh-token-expiration-time}")
    private val refreshTokenExpirationTime: Long,
) {

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    fun createAccessToken(authentication: Authentication): String {
        val now = Date()
        return makeToken(authentication, now, setAccessTokenExpirationTime(now))
    }

    fun createRefreshToken(authentication: Authentication): String {
        val now = Date()
        val refreshToken = makeToken(authentication, now, setRefreshToeknExpirationTime(now))
        saveForCheckingIsLogin(refreshToken)
        return refreshToken
    }

    fun getAuthentication(token: String): Authentication {
        val (auth, name, role) = checkEachClaimBy(getClaims(token))
        val authorities = getSimpleGrantedAuthorities(role)
        return UsernamePasswordAuthenticationToken(createPrincipa(name, role, auth, authorities), "", authorities)
    }

    fun validateToken(token: String): JwtCode {
        if (checkBySignatureKey(token)) return JwtCode.ACCESS
        return JwtCode.DENIED
    }

    private fun checkBySignatureKey(token: String): Boolean {
        try {
            getClaims(token)
            return true
        } catch (e: Exception) {
            when (e) {
                is SecurityException, is MalformedJwtException, is UnsupportedJwtException, is IllegalArgumentException -> {
                    JwtCode.DENIED
                }

                is ExpiredJwtException -> {
                    JwtCode.EXPIRED
                }
            }
            println(e.message)
        }
        return false
    }

    private fun createPrincipa(name: Any, role: Any, auth: String, authorities: List<SimpleGrantedAuthority>): CustomUser {
        val principal = CustomUser(
            name = name as String,
            role = role as String,
            loginId = auth,
            password = "",
            authorities = authorities,
        )
        return principal
    }

    private fun getSimpleGrantedAuthorities(role: Any): List<SimpleGrantedAuthority> {
        val authorities = (role as String)
            .split(",")
            .map { SimpleGrantedAuthority("ROLE_$it") }
        return authorities
    }

    private fun checkEachClaimBy(claims: Claims): Triple<String, Any, Any> {
        val auth = claims.subject ?: throw JwtClaimException(ErrorCode.JWT_CLAIM_EXCEPTION)
        val name = claims["name"] ?: throw JwtClaimException(ErrorCode.JWT_CLAIM_EXCEPTION)
        val role = claims["role"] ?: throw JwtClaimException(ErrorCode.JWT_CLAIM_EXCEPTION)
        return Triple(auth, name, role)
    }

    private fun setAccessTokenExpirationTime(now: Date): Date {
        val accessExpiration = Date(now.time + accessTokenExpirationTime)
        return accessExpiration
    }

    private fun setRefreshToeknExpirationTime(now: Date): Date {
        val reFreshExpiration = Date(now.time + refreshTokenExpirationTime)
        return reFreshExpiration
    }

    private fun makeToken(authentication: Authentication, now: Date, expiration: Date): String {
        val accessToken = Jwts.builder()
            .setSubject((authentication.principal as CustomUser).username)
            .claim("iss", "afterCompany")
            .claim("name", (authentication.principal as CustomUser).name)
            .claim("role", (authentication.principal as CustomUser).role)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
        return accessToken
    }

    private fun saveForCheckingIsLogin(refreshToken: String) {
        redisRepository.save(Token(token = refreshToken, value = "refreshToken"))
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
