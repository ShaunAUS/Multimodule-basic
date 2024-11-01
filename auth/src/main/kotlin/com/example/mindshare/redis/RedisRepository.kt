package com.example.mindshare.redis

import Token
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RedisRepository : CrudRepository<Token, String> {
    fun findByToken(token: String): Token?
}
