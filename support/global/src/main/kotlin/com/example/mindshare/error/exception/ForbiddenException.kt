package com.example.mindshare.error.exception

import com.example.mindshare.ErrorCode

class ForbiddenException(
    val errorCode: ErrorCode,
    val data: Any? = null,
) : RuntimeException(errorCode.message)
