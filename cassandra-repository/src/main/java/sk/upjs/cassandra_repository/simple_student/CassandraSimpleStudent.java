package sk.upjs.cassandra_repository.simple_student;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import sk.upjs.nosql_data_source.entity.SimpleStudent;
import sk.upjs.nosql_data_source.entity.Student;

@Table(value = "simple_student")
public class CassandraSimpleStudent {

	@PrimaryKey
	private Long id;

	@Column
	private String meno;

	@Column
	@Indexed
	private String priezvisko;

	@Transient
	private char kodpohlavie;

	@Column("titul")
	private String skratkaakadtitul;

	@Column("id_studii")
	private Set<Long> idStudii;

	public CassandraSimpleStudent() {

	}

	public CassandraSimpleStudent(SimpleStudent student) {
		id = student.getId();
		meno = student.getMeno();
		priezvisko = student.getPriezvisko();
		kodpohlavie = student.getKodpohlavie();
		skratkaakadtitul = student.getSkratkaakadtitul();
		idStudii = new HashSet<>(student.getIdStudii());
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

	public Set<Long> getIdStudii() {
		return idStudii;
	}

	public void setIdStudii(Set<Long> idStudii) {
		this.idStudii = idStudii;
	}

	@Override
	public String toString() {
		return "CassandraSimpleStudent [id=" + id + ", meno=" + meno + ", priezvisko=" + priezvisko + ", kodpohlavie="
				+ kodpohlavie + ", skratkaakadtitul=" + skratkaakadtitul + ", idStudii=" + idStudii + "]";
	}
}
