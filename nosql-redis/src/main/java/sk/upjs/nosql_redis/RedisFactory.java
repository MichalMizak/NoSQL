package sk.upjs.nosql_redis;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import sk.upjs.nosql_data_source.entity.SimpleStudent;
import sk.upjs.nosql_redis.hash.SimpleStudentDao;

public enum RedisFactory {

	INSTANCE;

	public static String HOSTNAME = "nosql.gursky.sk";
	public static String PASSWORD = "dh38fhw0238923df89djkd93la9fjs0mq9gjflv9jkddj934df90rj";

	public JedisConnectionFactory getJedisConnectionFactory() {
		RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(HOSTNAME);
		standaloneConfiguration.setPassword(PASSWORD);
		return new JedisConnectionFactory(standaloneConfiguration);
	}
	
	public RedisTemplate<String, SimpleStudent> getSimpleStudentTemplate() {
		RedisTemplate<String, SimpleStudent> template	= new RedisTemplate<>();
		template.setConnectionFactory(getJedisConnectionFactory());
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}
	
	public SimpleStudentDao getSimpleStudentDao() {
		return new SimpleStudentDao(getSimpleStudentTemplate());
	}
}
