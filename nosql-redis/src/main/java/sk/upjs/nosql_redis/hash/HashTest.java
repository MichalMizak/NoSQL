package sk.upjs.nosql_redis.hash;

import java.util.List;

import sk.upjs.nosql_data_source.entity.SimpleStudent;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.StudentDao;
import sk.upjs.nosql_redis.RedisFactory;

public class HashTest {

	public static void main(String[] args) {
		SimpleStudentDao redisDao = RedisFactory.INSTANCE.getSimpleStudentDao();
		StudentDao studentDao = DaoFactory.INSTANCE.getStudentDao();
		List<SimpleStudent> students = studentDao.getSimpleStudents();
		students.forEach(redisDao::saveStudent);
		System.out.println(redisDao.getAllStudents());
		students.forEach(s -> redisDao.removeStudent(s.getId()));
		System.out.println(redisDao.getAllStudents());
	}
}
