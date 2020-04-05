package sk.upjs.nosql_redis_repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import sk.upjs.nosql_data_source.entity.Student;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.StudentDao;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
		StudentRepository repository = context.getBean(StudentRepository.class);
		StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();
		List<Student> students = studentDao.getAll();
		RedisStudent redisStudent = new RedisStudent(students.get(0));
		repository.save(redisStudent);
		System.out.println("Student count: " + repository.count());

		List<RedisStudent> found = repository.findByPriezvisko("Najahalovu");
		System.out.println("Found student: " + found);
		repository.delete(redisStudent);
		List<RedisStudent> redisStudents = students.stream().map(s -> new RedisStudent(s)).collect(Collectors.toList());
		repository.saveAll(redisStudents);
		
		repository.findAll().forEach(s -> {
			if (s.getStudium().size() > 1)
				System.out.println(s.getId());
		});
		context.close();
	}
}
