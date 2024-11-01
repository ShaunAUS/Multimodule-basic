package com.example.mindshare.domain.auth

data class UserLoginResponse(
    val accessToken: String,
    val refreshToken: String,
)
