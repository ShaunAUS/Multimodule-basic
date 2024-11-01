package com.example.mindshare.template

import com.example.mindshare.ErrorCode
import com.example.mindshare.error.ErrorMessage

data class Response<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: ErrorMessage? = null,
) {
    companion object {
        fun success(): Response<Any> {
            return Response(ResultType.SUCCESS, null, null)
        }

        fun <S> success(data: S): Response<S> {
            return Response(ResultType.SUCCESS, data, null)
        }

        fun <S> error(error: ErrorCode, errorData: Any? = null): Response<S> {
            return Response(ResultType.ERROR, null, ErrorMessage(error, errorData))
        }
    }
}
