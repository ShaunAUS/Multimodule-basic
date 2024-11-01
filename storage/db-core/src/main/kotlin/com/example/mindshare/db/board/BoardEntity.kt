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
    @Column
    var title: String,
    @Column
    var content: String,
    @Column
    var viewCount: Int = 0,
    @Column(nullable = false, unique = true, updatable = false)
    var uniqueId: String = UUID.randomUUID().toString(),
) : BaseEntity() {

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
