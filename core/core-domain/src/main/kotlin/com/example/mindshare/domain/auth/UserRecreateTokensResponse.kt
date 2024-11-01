package com.example.mindshare.domain.auth

data class UserRecreateTokensResponse(
    val accessToken: String,
    val refreshToken: String,
)
