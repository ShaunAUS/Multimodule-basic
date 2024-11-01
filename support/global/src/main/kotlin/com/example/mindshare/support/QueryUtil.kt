package com.example.mindshare.support

import com.example.mindshare.ErrorCode
import com.example.mindshare.error.exception.BusinessException
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

// TODO 값만넣으면 Excpetion 다양하게 써보고 싶음
fun <T, Id> CrudRepository<T, Id>.findByIdOrThrow(id: Id): T {
    return this.findByIdOrNull(id) ?: throw BusinessException(ErrorCode.ALREADY_LOGOUT_USER_EXCEPTION)
}
