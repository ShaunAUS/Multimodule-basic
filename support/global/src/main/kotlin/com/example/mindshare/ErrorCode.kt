package com.example.mindshare

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val errorCode: String,
    val message: String,
) {

    // USER
    LOGOUT_USER_ACCESS_TOKEN_EXCPETION(HttpStatus.OK, "C001", "already logout user access token"),
    FORBIDDEN_USER_EXCEPTION(HttpStatus.OK, "C001", "forbidden user"),

    // JWT
    ONLY_REFRESH_TOKEN_EXCEPTION(HttpStatus.OK, "J001", "only refresh token"),
    INVALID_JWT_SIGNATURE_EXCEPTION(HttpStatus.OK, "J002", "invalid jwt signature"),
    EXPIRED_JWT_TOKEN_EXCEPTION(HttpStatus.OK, "J003", "expired jwt token"),
    INVALID_JWT_MALFORMED_EXCEPTION(HttpStatus.OK, "J004", "invalid jwt malformed"),
    JWT_CLAIM_EXCEPTION(HttpStatus.OK, "J005", "jwt claim exception"),
    EXPIRED_REFRESH_TOKEN_EXCEPTION(HttpStatus.OK, "J003", "Expired refresh token"),
    DENIED_REFRESH_TOKEN_EXCEPTION(HttpStatus.OK, "J004", "Denied refresh token"),

    // Validation
    INVALID_INPUT_VALUE(HttpStatus.OK, "V001", "Invalid Input Value"),

    // Authorization
    ACCESS_DENIED(HttpStatus.OK, "V001", "not enough authority"),

    // Auth
    ALREADY_LOGOUT_USER_EXCEPTION(HttpStatus.OK, "A001", "already logout user"),
}
