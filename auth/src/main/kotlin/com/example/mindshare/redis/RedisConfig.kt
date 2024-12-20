
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
class RedisConfig(
    @Value("\${spring.redis.host}")
    private val host: String,

    @Value("\${spring.redis.port}")
    private val port: Int,

) {
    @Bean
    fun redisConnection(): RedisConnectionFactory {
        return LettuceConnectionFactory(host, port)
    }
}
