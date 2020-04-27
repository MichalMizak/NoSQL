package sk.upjs.mongodb_project;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import sk.upjs.nosql_data_source.entity.Studium;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection = "studium")
public class MongoStudium {
	@Id
	private Long id;
	private Date zaciatokStudia;
	private Date koniecStudia;
	@Indexed(name = "spIndex", direction = IndexDirection.ASCENDING)
	private MongoStudijnyProgram studijnyProgram;

	public MongoStudium() {
	}

	public MongoStudium(Studium studium) {
		this.id = studium.getId();
		this.studijnyProgram = new MongoStudijnyProgram(studium.getStudijnyProgram());
		SimpleDateFormat formatter = new SimpleDateFormat("d.M.yyyy");
		try {
			this.zaciatokStudia = formatter.parse(studium.getZaciatokStudia());
			this.koniecStudia = formatter.parse(studium.getKoniecStudia());
		} catch (ParseException e) {
			// ignore
		}
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getZaciatokStudia() {
		return zaciatokStudia;
	}

	public void setZaciatokStudia(Date zaciatokStudia) {
		this.zaciatokStudia = zaciatokStudia;
	}

	public Date getKoniecStudia() {
		return koniecStudia;
	}

	public void setKoniecStudia(Date koniecStudia) {
		this.koniecStudia = koniecStudia;
	}

	public MongoStudijnyProgram getStudijnyProgram() {
		return studijnyProgram;
	}

	public void setStudijnyProgram(MongoStudijnyProgram studijnyProgram) {
		this.studijnyProgram = studijnyProgram;
	}

	@Override
	public String toString() {
		return "MongoStudium{" + "id=" + id + ", zaciatokStudia='" + zaciatokStudia + '\'' + ", koniecStudia='"
				+ koniecStudia + '\'' + ", studijnyProgram=" + studijnyProgram + '}';
	}
}
