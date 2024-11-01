package com.example.mindshare.domain.member

import com.example.mindshare.domain.member.dto.CreateMemberDto
import com.example.mindshare.domain.member.dto.MemberInfoDto
import com.example.mindshare.domain.member.dto.ModifyMemberDto
import org.springframework.stereotype.Service

@Service
class MemberServiceImpl(
    private val memberAppender: MemberAppender,
) : MemberService {

    override fun createMember(createMemberDto: CreateMemberDto): MemberInfoDto {
        return memberAppender.createMember(createMemberDto)
    }

    override fun modifyMember(modifyMemberDto: ModifyMemberDto): MemberInfoDto {
        return memberAppender.modifyMember(modifyMemberDto)
    }
}
