package com.example.mindshare.domain.board

import com.example.mindshare.domain.board.dto.BoardInfoDto
import com.example.mindshare.domain.board.dto.CreateBoardDto
import com.example.mindshare.domain.board.dto.ModifyBoardDto
import org.redisson.api.RedissonClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class BoardServiceImpl(
    private val boardFinder: BoardFinder,
    private val boardAppender: BoardAppender,
    private val redissonClient: RedissonClient,
) : BoardService {

    override fun modifyBoard(modifyBoardDto: ModifyBoardDto): BoardInfoDto {
        val lock = redissonClient.getLock("board:${modifyBoardDto.uniqueId}")
        try {
            // 4초 대기, 2초 락 유지
            val isLocked = lock.tryLock(4, 2, TimeUnit.SECONDS)
            if (!isLocked) {
                throw RuntimeException("게시판 수정 락 획득 실패")
            }

            // 락 획득 후 로직 수행
            return boardAppender.modifyBoard(modifyBoardDto)
        } catch (e: InterruptedException) {
            // 락 얻으려다가 인터럽트 발생 시
            e.printStackTrace()
            throw RuntimeException("modify board 락 인터럽트 발생", e)
        } finally {
            // 락 해제
            if (lock != null && lock.isLocked) {
                lock.unlock()
            }
        }
    }

    override fun createBoard(createBoardDto: CreateBoardDto): BoardInfoDto {

        //FIXME 나중에 만드는 userId로 lock 수정
        val lock = redissonClient.getLock("board:create")

        try {
            val isLocked = lock.tryLock(4, 2, TimeUnit.SECONDS)
            if (!isLocked) {
                throw RuntimeException("게시판 생성 락 획득 실패")
            }

            return boardAppender.createBoard(createBoardDto)

        } catch (e: InterruptedException) {
            e.printStackTrace()
            throw RuntimeException("create board 락 인터럽트 발생", e)
        } finally {
            if (lock != null && lock.isLocked) {
                lock.unlock()
            }

        }
    }

    override fun deleteBoard(uniqueId: String): BoardInfoDto {
        val lock = redissonClient.getLock("board:$uniqueId")

        try {
            val isLocked = lock.tryLock(4, 2, TimeUnit.SECONDS)

            if (!isLocked) {
                throw RuntimeException("게시판 삭제 락 획득 실패")
            }

            return boardAppender.deleteBoard(uniqueId)

        } catch (e: InterruptedException) {
            e.printStackTrace()
            throw RuntimeException("delete board 락 인터럽트 발생", e)
        } finally {
            if (lock != null && lock.isLocked) {
                lock.unlock()
            }
        }
    }

    override fun getBoard(boardUuid: String): BoardInfoDto {
        return boardFinder.getBoard(boardUuid)
    }

    override fun getBoards(pageable: Pageable): Page<BoardInfoDto> {
        return boardFinder.getBoards(pageable)
    }

    override fun searchBoard(searchTitle: String, pageable: Pageable): Page<BoardInfoDto> {
        return boardFinder.searchBoard(searchTitle, pageable)
    }
}
