package com.example.mindshare.redission

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component

@Aspect
@Component
class DistributedLockAspect(
    private val redissonClient: RedissonClient,
    private val requireNewTransactionAspect: RequireNewTransactionAspect,
) {
    companion object {
        private const val REDISSON_LOCK_PREFIX = "LOCK:"
    }

    /**
     * `@DistributedLock` 어노테이션이 선언된 메소드를 포인트컷으로 설정
     *
     * @param distributedLock 분산락 처리를 위한 어노테이션
     */
    @Pointcut("@annotation(distributedLock)")
    fun pointCut(distributedLock: DistributedLock) {
    }

    /**
     * 분산 락을 사용하여 메소드를 감싸는 Around 어드바이스
     *
     * @param joinPoint ProceedingJoinPoint, 원래의 메소드를 나타냄
     * @param distributedLock 분산락 어노테이션
     * @return 메소드 실행 결과
     * @throws Throwable 예외 처리
     */
    @Around(value = "pointCut(distributedLock)")
    fun around(joinPoint: ProceedingJoinPoint, distributedLock: DistributedLock): Any? {
        val key = REDISSON_LOCK_PREFIX + generateLockKey(joinPoint, distributedLock)

        // 분산락 시도
        val rLock = redissonClient.getLock(key)
        return try {
            // 락 획득 시도
            val available = rLock.tryLock(
                distributedLock.waitTime.toLong(),
                distributedLock.leaseTime.toLong(),
                distributedLock.timeUnit,
            )
            if (!available) {
                // 락 획득 실패 시 예외처리
                throw RuntimeException("락 획득 실패")
            }

            // 분산락을 획득하면 새로운 트랜잭션을 시작하여 비즈니스 로직 실행
            requireNewTransactionAspect.proceed(joinPoint)
        } catch (e: InterruptedException) {
            // 락 획득 중 인터럽트 발생 시 처리
            throw RuntimeException("락 획득중 인터럽트")
        } finally {
            // 분산락 해제
            if (rLock.isLocked && rLock != null) {
                rLock.unlock() // 락 해제
            }
        }
    }

    private fun generateLockKey(joinPoint: ProceedingJoinPoint, distributedLock: DistributedLock): String {
        val method = joinPoint.signature as MethodSignature
        val parameters = joinPoint.args
        val parameterNames = method.parameterNames

        val parser = SpelExpressionParser()
        val context = StandardEvaluationContext()

        // # = context에 설정된 변수 참조
        parameterNames.forEachIndexed { index, name ->
            context.setVariable(name, parameters[index])
        }

        // context = { 0 , update객체 }
        // expression = {"'modifyBoard:' + #modifyBoardDto.uniqueId"}
        return parser.parseExpression(distributedLock.key).getValue(context, String::class.java) ?: throw RuntimeException("락 키 생성 실패")
    }
}
