package sk.upjs.cassandra_repository.student;

import java.util.Collection;

import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;


public interface StudentRepository extends CrudRepository<CassandraStudent, MapId>{
	@AllowFiltering
	Collection<NamesOnly> findByskratkaakadtitul(String titul);
}
