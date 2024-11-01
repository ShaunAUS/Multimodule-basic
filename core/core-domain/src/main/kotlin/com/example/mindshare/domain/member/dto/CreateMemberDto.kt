package com.example.mindshare.domain.member.dto

import com.example.mindshare.support.ValidEnum
import core.enums.Role
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class CreateMemberDto(
    @field:NotBlank(message = "비밀번호는 필수 입력값입니다")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,20}\$",
        message = "비밀번호는 8~20자 영문, 숫자, 특수문자를 포함해야 합니다",
    )
    val password: String?,

    @field:NotBlank(message = "이름은 필수 입력값입니다")
    @field:Pattern(
        regexp = "^[가-힣a-zA-Z]{2,20}\$",
        message = "이름은 2~20자의 한글 또는 영문만 가능합니다",
    )
    val name: String?,

    @field:NotNull(message = "나이는 필수 입력값입니다")
    @field:Min(value = 1, message = "나이는 1살 이상이어야 합니다")
    @field:Max(value = 100, message = "나이는 100살 이하여야 합니다")
    val age: Int?,

    @field:NotBlank(message = "로그인 아이디는 필수 입력값입니다")
    @field:Pattern(
        regexp = "^[a-z0-9]{4,20}\$",
        message = "아이디는 4~20자의 영문 소문자, 숫자만 사용 가능합니다",
    )
    val loginId: String?,
    @field:NotNull(message = "역할은 필수 입력값입니다")
    @field:ValidEnum(enumClass = Role::class, message = "올바른 역할이 아닙니다")
    val role: Role?,

)
