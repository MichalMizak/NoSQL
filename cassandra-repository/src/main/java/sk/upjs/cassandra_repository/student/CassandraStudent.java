package sk.upjs.cassandra_repository.student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType.Name;

import sk.upjs.nosql_data_source.entity.Student;

@Table("student")
public class CassandraStudent {

	@PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private Long id;

	@PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
	private String priezvisko;

	@Column
	private String meno;

	@Transient
	private char kodpohlavie;

	@Column("titul")
	private String skratkaakadtitul;

	@Column("studia")
	@CassandraType(type = Name.LIST, userTypeName = "studium", typeArguments = { Name.UDT })
	private List<CassandraStudium> studium = new ArrayList<>();

	public CassandraStudent() {
		
	}
	
	public CassandraStudent(Student student) {
		super();
		this.id = student.getId();
		this.priezvisko = student.getPriezvisko();
		this.meno = student.getMeno();
		this.kodpohlavie = student.getKodpohlavie();
		this.skratkaakadtitul = student.getSkratkaakadtitul();
		this.studium = student.getStudium().stream().map(st -> new CassandraStudium(st)).collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPriezvisko() {
		return priezvisko;
	}

	public void setPriezvisko(String priezvisko) {
		this.priezvisko = priezvisko;
	}

	public String getMeno() {
		return meno;
	}

	public void setMeno(String meno) {
		this.meno = meno;
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

	public List<CassandraStudium> getStudium() {
		return studium;
	}

	public void setStudium(List<CassandraStudium> studium) {
		this.studium = studium;
	}

	@Override
	public String toString() {
		return "CassandraStudent [id=" + id + ", priezvisko=" + priezvisko + ", meno=" + meno + ", kodpohlavie="
				+ kodpohlavie + ", skratkaakadtitul=" + skratkaakadtitul + ", studium=" + studium + "]";
	}

}
