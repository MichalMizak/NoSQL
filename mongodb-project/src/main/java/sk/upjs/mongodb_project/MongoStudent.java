package sk.upjs.mongodb_project;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sk.upjs.nosql_data_source.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "students")
public class MongoStudent {
	@Id
	private Long id;
	private String meno;
	private String priezvisko;
	private char kodpohlavie;
	private String skratkaakadtitul;
	private List<MongoStudium> zoznamStudii = new ArrayList<>();

	public MongoStudent() {
	}

	public MongoStudent(Student student) {
		this.id = student.getId();
		this.meno = student.getMeno();
		this.priezvisko = student.getPriezvisko();
		this.kodpohlavie = student.getKodpohlavie();
		this.skratkaakadtitul = student.getSkratkaakadtitul();
		this.zoznamStudii = student.getStudium().stream().map(s -> new MongoStudium(s)).collect(Collectors.toList());
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

	public List<MongoStudium> getZoznamStudii() {
		return zoznamStudii;
	}

	public void setZoznamStudii(List<MongoStudium> zoznamStudii) {
		this.zoznamStudii = zoznamStudii;
	}

	@Override
	public String toString() {
		return "MongoStudent{" + "id=" + id + ", meno='" + meno + '\'' + ", priezvisko='" + priezvisko + '\''
				+ ", kodpohlavie=" + kodpohlavie + ", skratkaakadtitul='" + skratkaakadtitul + '\'' + ", zoznamStudii="
				+ zoznamStudii + '}';
	}
}
