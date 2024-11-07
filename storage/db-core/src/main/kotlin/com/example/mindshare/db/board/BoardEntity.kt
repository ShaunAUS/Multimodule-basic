package com.example.mindshare.db.board

import com.example.mindshare.db.BaseEntity
import com.example.mindshare.domain.board.dto.BoardInfoDto
import com.example.mindshare.domain.board.dto.CreateBoardDto
import com.example.mindshare.domain.board.dto.ModifyBoardDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(
    name = "board",
    indexes = [
        Index(name = "idx_board_uniqueId", columnList = "uniqueId"),
    ],
)
class BoardEntity(
    title: String,
    content: String,
    viewCount: Int = 0,
) : BaseEntity() {

    @Column
    var title: String = title
        protected set

    @Column
    var content: String = content
        protected set

    @Column
    var viewCount: Int = viewCount
        protected set

    @Column(nullable = false, unique = true, updatable = false)
    val uniqueId: String = UUID.randomUUID().toString()

    companion object {
        fun of(createBoardDto: CreateBoardDto): BoardEntity {
            return BoardEntity(
                title = createBoardDto.title!!,
                content = createBoardDto.content!!,
            )
        }
    }

    fun update(modifyBoardDto: ModifyBoardDto): BoardEntity {
        title = modifyBoardDto.title ?: title
        content = modifyBoardDto.content ?: content
        return this
    }

    fun toBoardInfoDto(): BoardInfoDto {
        return BoardInfoDto(this.title, this.content, this.viewCount, this.uniqueId)
    }

    fun delete() {
        this.deleteFlag = 'Y'
    }

    fun updateViewCount() {
        this.viewCount++
    }
}
