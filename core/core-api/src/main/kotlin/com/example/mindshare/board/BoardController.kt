package com.example.mindshare.board

import com.example.mindshare.domain.board.BoardService
import com.example.mindshare.domain.board.dto.BoardInfoDto
import com.example.mindshare.domain.board.dto.CreateBoardDto
import com.example.mindshare.domain.board.dto.ModifyBoardDto
import com.example.mindshare.template.Response
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/board")
class BoardController(
    private val boardService: BoardService,

) {

    @PostMapping("")
    fun createBoard(@RequestBody @Valid createBoardDto: CreateBoardDto): ResponseEntity<Response<BoardInfoDto>> {
        return ResponseEntity.ok(Response.success(boardService.createBoard(createBoardDto)))
    }

    @GetMapping("/{board-uuid}")
    fun getBoard(@PathVariable("board-uuid") boardUuid: String): ResponseEntity<Response<BoardInfoDto>> {
        return ResponseEntity.ok(Response.success(boardService.getBoard(boardUuid)))
    }

    @PatchMapping("")
    fun modifyBoard(@RequestBody @Valid modifyBoardDto: ModifyBoardDto): ResponseEntity<Response<BoardInfoDto>> {
        return ResponseEntity.ok(Response.success(boardService.modifyBoard(modifyBoardDto)))
    }

    @PatchMapping("/{board-uuid}")
    fun deleteBoard(@PathVariable("board-uuid") boardUuid: String): ResponseEntity<Response<BoardInfoDto>> {
        return ResponseEntity.ok(Response.success(boardService.deleteBoard(boardUuid)))
    }

    @GetMapping("/all")
    fun getBoards(
        @PageableDefault(sort = ["id"], direction = Sort.Direction.ASC, size = 10) pageable: Pageable,
    ): ResponseEntity<Response<Page<BoardInfoDto>>> {
        return ResponseEntity.ok(Response.success(boardService.getBoards(pageable)))
    }

    @GetMapping("/search")
    fun serachBoard(
        @RequestParam searchTitle: String,
        @PageableDefault(sort = ["id"], direction = Sort.Direction.ASC, size = 10) pageable: Pageable,
    ): ResponseEntity<Response<Page<BoardInfoDto>>> {
        return ResponseEntity.ok(Response.success(boardService.searchBoard(searchTitle, pageable)))
    }
}
