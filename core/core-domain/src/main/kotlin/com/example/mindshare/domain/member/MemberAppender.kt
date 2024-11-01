package com.example.mindshare.domain.member

import com.example.mindshare.domain.member.dto.CreateMemberDto
import com.example.mindshare.domain.member.dto.MemberInfoDto
import com.example.mindshare.domain.member.dto.ModifyMemberDto
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MemberAppender(
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun createMember(createMemberDto: CreateMemberDto): MemberInfoDto {
        return memberRepository.createMember(createMemberDto)
    }

    @Transactional
    fun modifyMember(modifyMemberDto: ModifyMemberDto): MemberInfoDto {
        return memberRepository.modifyMember(modifyMemberDto)
    }
}
