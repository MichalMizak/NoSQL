package sk.upjs.cassandra_repository.student;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.stereotype.Service;

import sk.upjs.cassandra_repository.simple_student.CassandraSimpleStudent;
import sk.upjs.nosql_data_source.entity.SimpleStudent;
import sk.upjs.nosql_data_source.entity.Student;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.StudentDao;

@Service
public class StudentRepositoryService {

	@Autowired
	StudentRepository repository;

	private StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();

	public void insertAllStudents() {
		List<Student> students = studentDao.getAll();
		List<CassandraStudent> cs = students.stream().map(s -> {
			return new CassandraStudent(s);
		}).collect(Collectors.toList());

		repository.saveAll(cs);
	}

	public void printStudents() {
		repository.findAll().forEach(System.out::println);
	}

	public void deleteAllStudents() {
		repository.deleteAll();
	}

	public Collection<NamesOnly> getByTitul(String titul) {
		return repository.findByskratkaakadtitul(titul);
	}

	public CassandraStudent findByIdAndPriezvisko(Long id, String priezvisko) {
		MapId mapId = BasicMapId.id("id", id).with("priezvisko", priezvisko);
		return repository.findById(mapId).orElse(null);
	}
}
