package com.example.mindshare.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    val name: String,
    val role: String,
    loginId: String,
    password: String,
    authorities: Collection<GrantedAuthority>,
) : User(loginId, password, authorities)
