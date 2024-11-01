package com.example.mindshare.error

import com.example.mindshare.ErrorCode

data class ErrorMessage private constructor(
    val code: String,
    val message: String,
    val data: Any? = null,
) {
    constructor(errorCode: ErrorCode, data: Any? = null) : this(
        code = errorCode.errorCode,
        message = errorCode.message,
        data = data,
    )
}
