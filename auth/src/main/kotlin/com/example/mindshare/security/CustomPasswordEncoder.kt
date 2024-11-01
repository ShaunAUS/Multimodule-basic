package com.example.mindshare.security

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class CustomPasswordEncoder(
    private val passwordEncoder: PasswordEncoder,
) {
    fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }
}
