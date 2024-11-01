package com.example.mindshare.domain.board.dto

data class BoardInfoDto(
    val title: String,
    val content: String,
    val viewCount: Int,
    val uniqueId: String,
)
