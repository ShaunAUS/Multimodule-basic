package com.example.mindshare.db.board

import com.example.mindshare.db.board.QBoardEntity.boardEntity
import com.example.mindshare.domain.board.BoardRepository
import com.example.mindshare.domain.board.dto.BoardInfoDto
import com.example.mindshare.domain.board.dto.CreateBoardDto
import com.example.mindshare.domain.board.dto.ModifyBoardDto
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils

@Repository
internal class BoardEntityRepository(
    private val boardJpaRepository: BoardJpaRepository,
    private val query: JPAQueryFactory,

) : BoardRepository {
    override fun createBoard(createBoardDto: CreateBoardDto): BoardInfoDto {
        val savedBoard = boardJpaRepository.save(BoardEntity.of(createBoardDto))
        return savedBoard.toBoardInfoDto()
    }

    override fun modifyBoard(modifyBoardDto: ModifyBoardDto): BoardInfoDto {
        val boardByUniqueId = query
            .selectFrom(boardEntity)
            .where(boardEntity.uniqueId.eq(modifyBoardDto.uniqueId))
            .fetchOne() ?: throw NoSuchElementException("Board not found")

        // 명시성을 위한 save
        val updatedBoard = boardJpaRepository.save(boardByUniqueId.update(modifyBoardDto))
        return updatedBoard.toBoardInfoDto()
    }

    override fun deleteBoard(uniqueId: String): BoardInfoDto {
        val boardByUniqueId = query
            .selectFrom(boardEntity)
            .where(boardEntity.uniqueId.eq(uniqueId))
            .fetchOne() ?: throw NoSuchElementException("Board not found")

        // 명시성을 위한 save
        boardByUniqueId.delete()
        boardJpaRepository.save(boardByUniqueId)
        return boardByUniqueId.toBoardInfoDto()
    }

    override fun getBoard(boardUuid: String): BoardInfoDto {
        val boardByUuid = query
            .selectFrom(boardEntity)
            .where(boardEntity.uniqueId.eq(boardUuid))
            .fetchOne() ?: throw NoSuchElementException("Board not found")
        boardByUuid.updateViewCount()
        boardJpaRepository.save(boardByUuid)

        return boardByUuid.toBoardInfoDto()
    }

    override fun getBoards(pageable: Pageable): Page<BoardInfoDto> {
        val boards = query
            .selectFrom(boardEntity)
            .where(boardEntity.deleteFlag.eq('N'))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
            .map { it.toBoardInfoDto() }

        return PageableExecutionUtils.getPage(boards, pageable) { countQuery() ?: 0L }
    }

    override fun searchBoard(searchTitle: String, pageable: Pageable): Page<BoardInfoDto> {
        val fetch = query
            .selectFrom(boardEntity)
            .where(
                boardEntity.deleteFlag.eq('N')
                    .and(searchTitleEq(searchTitle)),
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
            .map { it.toBoardInfoDto() }

        return PageableExecutionUtils.getPage(fetch, pageable) { countQueryByTitle(searchTitle) ?: 0L }
    }

    private fun searchTitleEq(searchTitle: String): BooleanExpression? {
        return if (StringUtils.hasText(searchTitle)) boardEntity.title.contains(searchTitle) else null
    }

    private fun countQuery(): Long? {
        return query
            .select(boardEntity.count())
            .from(boardEntity)
            .where(boardEntity.deleteFlag.eq('N'))
            .fetchOne()
    }

    private fun countQueryByTitle(searchTitle: String): Long? {
        return query
            .select(boardEntity.count())
            .from(boardEntity)
            .where(
                boardEntity.deleteFlag.eq('N')
                    .and(searchTitleEq(searchTitle)),
            )
            .fetchOne()
    }
}
