package com.example.mindshare.domain.board

import com.example.mindshare.domain.board.dto.BoardInfoDto
import com.example.mindshare.domain.board.dto.CreateBoardDto
import com.example.mindshare.domain.board.dto.ModifyBoardDto
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BoardAppender(
    private val boardRepository: BoardRepository,

) {
    @Transactional
    fun createBoard(createBoardDto: CreateBoardDto): BoardInfoDto {
        return boardRepository.createBoard(createBoardDto)
    }

    @Transactional
    fun modifyBoard(modifyBoardDto: ModifyBoardDto): BoardInfoDto {
        return boardRepository.modifyBoard(modifyBoardDto)
    }

    @Transactional
    fun deleteBoard(uniqueId: String): BoardInfoDto {
        return boardRepository.deleteBoard(uniqueId)
    }
}
