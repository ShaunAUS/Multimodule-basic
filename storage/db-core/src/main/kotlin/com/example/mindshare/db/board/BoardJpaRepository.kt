package com.example.mindshare.db.board

import org.springframework.data.jpa.repository.JpaRepository

interface BoardJpaRepository : JpaRepository<BoardEntity, Long>
