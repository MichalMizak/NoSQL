package sk.upjs.nosql_redis_repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<RedisStudent, Long>{
	List<RedisStudent> findByPriezvisko(String priezvisko);
}
