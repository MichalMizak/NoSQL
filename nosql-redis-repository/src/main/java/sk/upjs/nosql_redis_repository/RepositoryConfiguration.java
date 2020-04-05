package sk.upjs.nosql_redis_repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RepositoryConfiguration {

	public static String HOSTNAME = "nosql.gursky.sk";
	public static String PASSWORD = "dh38fhw0238923df89djkd93la9fjs0mq9gjflv9jkddj934df90rj";

	@Bean
	public RedisConnectionFactory getConnectionFactory() {
		RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(HOSTNAME);
		standaloneConfiguration.setPassword(PASSWORD);
		LettuceConnectionFactory result = new LettuceConnectionFactory(standaloneConfiguration);
//		result.afterPropertiesSet();
		return result;
	}

	@Bean
	public RedisTemplate<?, ?> getTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<byte[], byte[]> template	= new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setDefaultSerializer(new StringRedisSerializer());
//		template.afterPropertiesSet();
		return template;
	}
}
