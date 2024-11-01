package com.example.mindshare.security

import com.example.mindshare.domain.member.MemberFinder
import com.example.mindshare.jwt.CustomUser
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomDetailsService(
    private val memberFinder: MemberFinder,
) : UserDetailsService {

    override fun loadUserByUsername(loginId: String): UserDetails {
        return memberFinder.checkExistedMember(loginId)
            ?.let { createUserDetails(it) } ?: throw IllegalArgumentException("유저 정보가 잘못되었습니다")
    }

    private fun createUserDetails(member: com.example.mindshare.domain.member.Member): UserDetails {
        return CustomUser(
            name = member.name,
            role = member.role,
            loginId = member.loginId,
            password = member.password,
            authorities = listOf(SimpleGrantedAuthority(member.role)),
        )
    }
}
