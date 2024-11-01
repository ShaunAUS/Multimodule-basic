
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "token", timeToLive = 1860) // 31분
class Token(
    @Id
    val token: String,
    val value: String,
)
