package com.example.mindshare.domain.member

import com.example.mindshare.domain.member.dto.CreateMemberDto
import com.example.mindshare.domain.member.dto.MemberInfoDto
import com.example.mindshare.domain.member.dto.ModifyMemberDto

interface MemberService {
    fun createMember(createMemberDto: CreateMemberDto): MemberInfoDto
    fun modifyMember(modifyMemberDto: ModifyMemberDto): MemberInfoDto
}
