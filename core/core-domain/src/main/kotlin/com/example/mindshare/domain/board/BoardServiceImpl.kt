package com.example.mindshare.domain.board

import com.example.mindshare.domain.board.dto.BoardInfoDto
import com.example.mindshare.domain.board.dto.CreateBoardDto
import com.example.mindshare.domain.board.dto.ModifyBoardDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BoardServiceImpl(
    private val boardFinder: BoardFinder,
    private val boardAppender: BoardAppender,

) : BoardService {
    override fun createBoard(createBoardDto: CreateBoardDto): BoardInfoDto {
        return boardAppender.createBoard(createBoardDto)
    }

    override fun getBoard(boardUuid: String): BoardInfoDto {
        return boardFinder.getBoard(boardUuid)
    }

    override fun modifyBoard(modifyBoardDto: ModifyBoardDto): BoardInfoDto {
        return boardAppender.modifyBoard(modifyBoardDto)
    }

    override fun deleteBoard(uniqueId: String): BoardInfoDto {
        return boardAppender.deleteBoard(uniqueId)
    }

    override fun getBoards(pageable: Pageable): Page<BoardInfoDto> {
        return boardFinder.getBoards(pageable)
    }

    override fun searchBoard(searchTitle: String, pageable: Pageable): Page<BoardInfoDto> {
        return boardFinder.searchBoard(searchTitle, pageable)
    }
}
