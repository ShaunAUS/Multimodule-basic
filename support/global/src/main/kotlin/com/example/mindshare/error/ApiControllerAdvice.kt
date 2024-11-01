package com.example.mindshare.error

import com.example.mindshare.ErrorCode
import com.example.mindshare.error.exception.BusinessException
import com.example.mindshare.error.exception.RedisLogOutException
import com.example.mindshare.template.Response
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.MalformedJwtException
import org.springframework.http.ResponseEntity
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.security.SignatureException

@RestControllerAdvice
class ApiControllerAdvice {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<Response<Any>> {
        return ResponseEntity.ok(Response.error(e.errorCode, e.data))
    }

    /**
     * JWT 변조 발생
     */
    @ExceptionHandler(SignatureException::class)
    fun handleJwtSignatureException(e: SignatureException): ResponseEntity<Response<Any>> {
        logger.error(e) { "JWT SignatureException" }
        val message = ErrorCode.INVALID_JWT_SIGNATURE_EXCEPTION.message
        val errorCode = ErrorCode.INVALID_JWT_SIGNATURE_EXCEPTION
        return ResponseEntity.ok(Response.error(errorCode, message))
    }

    /**
     * JWT 토큰으로 아무 값이나 사용하는경우
     */
    @ExceptionHandler(MalformedJwtException::class)
    fun handleJwtMalformedException(e: MalformedJwtException): ResponseEntity<Response<Any>> {
        logger.error(e) { "JWT MalformedJwtException" }
        val message = ErrorCode.INVALID_JWT_MALFORMED_EXCEPTION.message
        val errorCode = ErrorCode.INVALID_JWT_MALFORMED_EXCEPTION
        return ResponseEntity.ok(Response.error(errorCode, message))
    }

    /**
     * JWT 토큰 만료
     */

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleJwtExpiredException(e: ExpiredJwtException): ResponseEntity<Response<Any>> {
        logger.error(e) { "JWT ExpiredJwtException" }
        val message = ErrorCode.EXPIRED_JWT_TOKEN_EXCEPTION.message
        val errorCode = ErrorCode.EXPIRED_JWT_TOKEN_EXCEPTION
        return ResponseEntity.ok(Response.error(errorCode, message))
    }

    /**
     * JWT 토큰 예외
     */
    @ExceptionHandler(JwtException::class)
    fun handleJwtException(e: JwtException): ResponseEntity<Response<Any>> {
        logger.error(e) { "JWT JwtException" }
        val message = ErrorCode.INVALID_JWT_SIGNATURE_EXCEPTION.message
        val errorCode = ErrorCode.INVALID_JWT_SIGNATURE_EXCEPTION
        return ResponseEntity.ok(Response.error(errorCode, message))
    }

    /**
     * Validation 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<Response<Any>> {
        logger.error(e) { "Validation Exception" }

        val errorMessage = e.bindingResult.fieldErrors
            .firstOrNull()?.defaultMessage
            ?: ErrorCode.INVALID_INPUT_VALUE.message

        return ResponseEntity.ok(
            Response.error(
                ErrorCode.INVALID_INPUT_VALUE,
                errorMessage,
            ),
        )
    }

    /**
     * 접근 권한이 없는 경우 AccessDeniedException
     * 근데 이걸로 한번에 잡아버리면 JWTAuthenticatioFilter에서 발생한 예외가 다 일로감
     */
    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(e: AuthorizationDeniedException): ResponseEntity<Response<Any>> {
        logger.error(e) { "Authorization Denied Exception" }
        val message = ErrorCode.ACCESS_DENIED.message
        val errorCode = ErrorCode.ACCESS_DENIED
        return ResponseEntity.ok(Response.error(errorCode, message))
    }

    @ExceptionHandler(RedisLogOutException::class)
    fun handleRedisLogOutException(e: RedisLogOutException): ResponseEntity<Response<Any>> {
        return ResponseEntity.ok(Response.error(e.errorCode, e.data))
    }
}
