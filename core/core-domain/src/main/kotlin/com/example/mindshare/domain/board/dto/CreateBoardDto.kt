package com.example.mindshare.domain.board.dto

import jakarta.validation.constraints.NotBlank

data class CreateBoardDto(
    @field:NotBlank(message = "제목은 필수 입력값입니다")
    val title: String?,
    @field:NotBlank(message = "내용은 필수 입력값입니다")
    val content: String?,
)
