package sk.upjs.nosql_redis.hash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import sk.upjs.nosql_data_source.entity.SimpleStudent;

public class SimpleStudentDao {

	private static final String KEY = "SimpleStudentMM";

	private HashOperations<String, Long, SimpleStudent> hashOps;
	private RedisTemplate<String, SimpleStudent> redisTemplate;

	public SimpleStudentDao(RedisTemplate<String, SimpleStudent> redisTemplate) {
		this.redisTemplate = redisTemplate;
		hashOps = redisTemplate.opsForHash();
	}

	public void saveStudent(SimpleStudent simpleStudent) {
		hashOps.put(KEY, simpleStudent.getId(), simpleStudent);
	}

	public SimpleStudent getStudentById(Long id) {
		return hashOps.get(KEY, id);
	}

	public Map<Long, SimpleStudent> getAllStudentsMap() {
		return hashOps.entries(KEY);
	}
	
	public List<SimpleStudent> getAllStudents() {
		return new ArrayList<>(hashOps.entries(KEY).values());
	}
	
	public void removeStudent(Long id) {
		hashOps.delete(KEY, id);
	}
}
