package com.example.mindshare.security

import com.example.mindshare.domain.auth.LoginRequest
import com.example.mindshare.domain.auth.UserLoginResponse
import com.example.mindshare.domain.auth.UserRecreateTokensResponse

interface AuthenticationService {
    fun login(loginRequest: LoginRequest): UserLoginResponse
    fun logout(accessToken: String, refreshToken: String)
    fun reCreateBothTokens(accessToken: String, refreshToken: String): UserRecreateTokensResponse
}
