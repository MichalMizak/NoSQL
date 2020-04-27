package sk.upjs.mongodb_project;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sk.upjs.nosql_data_source.entity.StudijnyProgram;

@Document(collection = "studijnyProgram")
public class MongoStudijnyProgram {

	@Id
	private Long id;
	private String skratka;
	private String popis;

	public MongoStudijnyProgram() {
	}

	public MongoStudijnyProgram(StudijnyProgram studijnyProgram) {
		this.id = studijnyProgram.getId();
		this.skratka = studijnyProgram.getSkratka();
		this.popis = studijnyProgram.getPopis();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSkratka() {
		return skratka;
	}

	public void setSkratka(String skratka) {
		this.skratka = skratka;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	@Override
	public String toString() {
		return "MongoStudijnyProgram{" + "id=" + id + ", skratka='" + skratka + '\'' + ", popis='" + popis + '\'' + '}';
	}
}
