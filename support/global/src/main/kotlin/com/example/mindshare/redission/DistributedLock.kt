package com.example.mindshare.redission

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(
    // 분산락 key
    val key: String,

    // 대기시간
    val waitTime: Int = 10,

    // 소유시간
    val leaseTime: Int = 5,

    // TimeUnit
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
)
