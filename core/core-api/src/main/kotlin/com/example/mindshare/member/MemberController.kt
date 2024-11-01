package com.example.mindshare.member

import com.example.mindshare.domain.member.MemberService
import com.example.mindshare.domain.member.dto.CreateMemberDto
import com.example.mindshare.domain.member.dto.MemberInfoDto
import com.example.mindshare.domain.member.dto.ModifyMemberDto
import com.example.mindshare.template.Response
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/member")
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping("")
    fun addMember(@RequestBody @Valid createMemberDto: CreateMemberDto): ResponseEntity<Response<MemberInfoDto>> {
        return ResponseEntity.ok(Response.success(memberService.createMember(createMemberDto)))
    }

    @PatchMapping("")
    fun modifyMember(@RequestBody @Valid modifyMemberDto: ModifyMemberDto): ResponseEntity<Response<MemberInfoDto>> {
        return ResponseEntity.ok(Response.success(memberService.modifyMember(modifyMemberDto)))
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('STUDENT')")
    fun modifyMember(): ResponseEntity<Response<String>> {
        return ResponseEntity.ok(Response.success("시큐리티 권한 테스트 성공 입니다"))
    }
}
