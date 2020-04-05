package sk.upjs.nosql_redis_repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import sk.upjs.nosql_data_source.entity.Student;
import sk.upjs.nosql_data_source.entity.Studium;

@RedisHash("redisStudentsMM")
public class RedisStudent {
	
	@Id
	private Long id;
	
	private String meno;

	@Indexed
	private String priezvisko;
	private char kodpohlavie;
	private String skratkaakadtitul;
	private List<Studium> studium = new ArrayList<Studium>();
	
	
	public RedisStudent() {
		
	}
	
	public RedisStudent(Student student) {
		id = student.getId();
		meno = student.getMeno();
		priezvisko = student.getPriezvisko();
		kodpohlavie = student.getKodpohlavie();
		skratkaakadtitul = student.getSkratkaakadtitul();
		studium = student.getStudium();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMeno() {
		return meno;
	}
	public void setMeno(String meno) {
		this.meno = meno;
	}
	public String getPriezvisko() {
		return priezvisko;
	}
	public void setPriezvisko(String priezvisko) {
		this.priezvisko = priezvisko;
	}
	public char getKodpohlavie() {
		return kodpohlavie;
	}
	public void setKodpohlavie(char kodpohlavie) {
		this.kodpohlavie = kodpohlavie;
	}
	public String getSkratkaakadtitul() {
		return skratkaakadtitul;
	}
	public void setSkratkaakadtitul(String skratkaakadtitul) {
		this.skratkaakadtitul = skratkaakadtitul;
	}
	public List<Studium> getStudium() {
		return studium;
	}
	public void setStudium(List<Studium> studium) {
		this.studium = studium;
	}

	@Override
	public String toString() {
		return "RedisStudent [id=" + id + ", meno=" + meno + ", priezvisko=" + priezvisko + ", kodpohlavie="
				+ kodpohlavie + ", skratkaakadtitul=" + skratkaakadtitul + ", studium=" + studium + "]";
	}
	
}
