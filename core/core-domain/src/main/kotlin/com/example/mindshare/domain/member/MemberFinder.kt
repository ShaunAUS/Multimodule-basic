package com.example.mindshare.domain.member

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MemberFinder(
    private val memberRepository: MemberRepository,
) {
    @Transactional(readOnly = true)
    fun checkExistedMember(loginId: String): com.example.mindshare.domain.member.Member? {
        return memberRepository.checkExistedMember(loginId)
    }
}
