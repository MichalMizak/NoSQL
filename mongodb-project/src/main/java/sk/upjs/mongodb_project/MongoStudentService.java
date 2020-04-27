package sk.upjs.mongodb_project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.stereotype.Service;

import sk.upjs.nosql_data_source.entity.Student;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.StudentDao;

@Service
public class MongoStudentService {
	@Autowired
	private MongoStudentRepository repository;
	@Autowired
	private MongoTemplate template;

	private StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();

	public void findByPriezvisko(String priezvisko) {
		repository.findByPriezvisko(priezvisko).forEach(System.out::println);
	}

	public void initDatabase() {
		List<Student> students = studentDao.getAll();
		List<MongoStudent> mongoStudents = students.stream().map(s -> new MongoStudent(s)).collect(Collectors.toList());
		System.out.println("mongo students created");
		repository.saveAll(mongoStudents);
		System.out.println("mongo students saved");
	}

	public void deleteAll() {
		repository.deleteAll();
	}

	public void printAll() {
		repository.findAll().forEach(System.out::println);
	}

	public void printMenoAPriezviskoByTitul(String titul) {
		List<MenoAPriezvisko> list = repository.findMenoAPriezviskoBySkratkaakadtitul(titul);
		list.forEach(System.out::println);
	}

	public long findByStudijnyProgramAndYear(MongoStudijnyProgram program, int year) {
		SimpleDateFormat formatter = new SimpleDateFormat("d.M.yyyy");
		Date date = null;
		try {
			date = formatter.parse("1.1." + year);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long start = System.nanoTime();
		List<MongoStudent> all = repository.findMongoStudentByStudijnyProgramAndYear(program, date);
		long duration = (System.nanoTime() - start) / 1000000; 
		all.forEach(System.out::println);
		return duration;
	}

	public List<MongoStudent> findAll() {
		return repository.findAll();
	}

	public void deleteCollections() {
		template.dropCollection("students");
		template.dropCollection("studijnyProgram");
	}

	public void printIndexes() {
		System.out.println("Current indices: ");
		List<IndexInfo> indexInfo = template.indexOps(MongoStudent.class).getIndexInfo();
		indexInfo.forEach(System.out::println);
	}

	public void dropIndexes() {
		template.getCollection("students").dropIndexes();
	}
}
