package com.example.mindshare.domain.board

import com.example.mindshare.domain.board.dto.BoardInfoDto
import com.example.mindshare.domain.board.dto.CreateBoardDto
import com.example.mindshare.domain.board.dto.ModifyBoardDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BoardService {
    fun createBoard(createBoardDto: CreateBoardDto): BoardInfoDto
    fun getBoard(boardUuid: String): BoardInfoDto
    fun modifyBoard(modifyBoardDto: ModifyBoardDto): BoardInfoDto
    fun deleteBoard(boardUuid: String): BoardInfoDto
    fun getBoards(pageable: Pageable): Page<BoardInfoDto>
    fun searchBoard(searchTitle: String, pageable: Pageable): Page<BoardInfoDto>
}
