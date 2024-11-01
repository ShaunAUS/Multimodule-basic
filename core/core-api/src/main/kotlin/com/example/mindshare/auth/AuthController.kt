package com.example.mindshare.auth

import com.example.mindshare.domain.auth.LoginRequest
import com.example.mindshare.domain.auth.UserLoginResponse
import com.example.mindshare.domain.auth.UserRecreateTokensResponse
import com.example.mindshare.security.AuthenticationService
import com.example.mindshare.template.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationService: AuthenticationService,

    @Value("\${jwt.headers.authorization}")
    private val authorizationHeader: String,
) {

    // @RequestBody 는 컴파일 , yml 은 런타임
    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val REFRESH_HEADER = "Refresh"
    }

    @PostMapping("login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Response<UserLoginResponse>> {
        val createTokenInfo = authenticationService.login(loginRequest)
        return ResponseEntity.ok(Response.success(createTokenInfo))
    }

    @PostMapping("/logout")
    fun logOut(@RequestHeader(AUTHORIZATION_HEADER) accessToken: String, @RequestHeader(REFRESH_HEADER) refreshToken: String): String {
        authenticationService.logout(accessToken, refreshToken)
        return "logoutPageUrl"
    }

    @PostMapping("/reissue")
    fun reissueBothToken(
        @RequestHeader(AUTHORIZATION_HEADER) accessToken: String,
        @RequestHeader(REFRESH_HEADER) refreshToken: String,
    ): ResponseEntity<Response<UserRecreateTokensResponse>> {
        return ResponseEntity.ok(Response.success(authenticationService.reCreateBothTokens(accessToken, refreshToken)))
    }
}
