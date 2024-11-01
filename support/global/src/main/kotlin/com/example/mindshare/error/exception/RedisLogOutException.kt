package com.example.mindshare.error.exception

import com.example.mindshare.ErrorCode

class RedisLogOutException(
    val errorCode: ErrorCode,
    val data: Any? = null,
) : RuntimeException(errorCode.message)
