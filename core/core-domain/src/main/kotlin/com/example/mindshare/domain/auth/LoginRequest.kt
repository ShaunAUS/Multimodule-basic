package com.example.mindshare.domain.auth

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Id는 필수 입력값입니다")
    val loginId: String?,
    @field:NotBlank(message = "비밀번호는 필수 입력값입니다")
    val password: String?,
)
