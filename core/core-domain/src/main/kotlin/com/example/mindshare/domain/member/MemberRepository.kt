package com.example.mindshare.domain.member

import com.example.mindshare.domain.member.dto.CreateMemberDto
import com.example.mindshare.domain.member.dto.MemberInfoDto
import com.example.mindshare.domain.member.dto.ModifyMemberDto

interface MemberRepository {
    fun createMember(createMemberDto: CreateMemberDto): MemberInfoDto
    fun modifyMember(modifyMemberDto: ModifyMemberDto): MemberInfoDto
    fun checkExistedMember(loginId: String): com.example.mindshare.domain.member.Member?
}
