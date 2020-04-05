package sk.upjs.cassandra_repository.simple_student;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.util.internal.SocketUtils;
import sk.upjs.nosql_data_source.entity.SimpleStudent;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.StudentDao;

@Service
public class SimpleStudentRepositoryService {

	@Autowired
	private SimpleStudentRepository repository;

	private StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();

	public void insertOneStudent() {
		List<SimpleStudent> students = studentDao.getSimpleStudents();

		CassandraSimpleStudent cStudent = new CassandraSimpleStudent(students.get(0));
		repository.save(cStudent);
	}

	public void insertAllStudents() {
		List<SimpleStudent> students = studentDao.getSimpleStudents();
		List<CassandraSimpleStudent> cs = students.stream().map(s -> {
			return new CassandraSimpleStudent(s);
		}).collect(Collectors.toList());
		
		repository.saveAll(cs);
	}

	public void printStudents() {
		repository.findAll().forEach(System.out::println);
	}
	
	public void deleteAllStudents() {
		repository.deleteAll();
	}
	
	public List<CassandraSimpleStudent> findByPriezvisko(String priezvisko) {
		return repository.findByPriezvisko(priezvisko);
	}
	
	public List<CassandraSimpleStudent> findByTitul(String titul) {
		return repository.findBySkratkaakadtitul(titul);
	}
	
	public void printAllStudentsByStream() {
		repository.findAllBy().forEach(System.out::println);;
	}
}
