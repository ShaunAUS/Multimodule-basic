package com.example.mindshare.db.member

import org.springframework.data.jpa.repository.JpaRepository

internal interface MemberJpaRepository : JpaRepository<MemberEntity, Long>
