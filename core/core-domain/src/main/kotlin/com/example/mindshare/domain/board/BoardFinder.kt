package com.example.mindshare.domain.board

import com.example.mindshare.domain.board.dto.BoardInfoDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BoardFinder(
    private val boardRepository: BoardRepository,

) {
    @Transactional(readOnly = true)
    fun getBoard(boardUuid: String): BoardInfoDto {
        return boardRepository.getBoard(boardUuid)
    }

    @Transactional(readOnly = true)
    fun getBoards(pageable: Pageable): Page<BoardInfoDto> {
        return boardRepository.getBoards(pageable)
    }

    @Transactional(readOnly = true)
    fun searchBoard(searchTitle: String, pageable: Pageable): Page<BoardInfoDto> {
        return boardRepository.searchBoard(searchTitle, pageable)
    }
}
