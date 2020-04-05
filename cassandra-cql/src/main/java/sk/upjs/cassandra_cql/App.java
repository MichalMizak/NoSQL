package sk.upjs.cassandra_cql;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.cql.ResultSetExtractor;
import org.springframework.data.cassandra.core.cql.RowMapper;
import org.springframework.data.cassandra.core.cql.generator.CreateTableCqlGenerator;
import org.springframework.data.cassandra.core.cql.generator.DropTableCqlGenerator;
import org.springframework.data.cassandra.core.cql.keyspace.CreateTableSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropTableSpecification;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.DriverException;

public class App {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CassandraConfig.class);

		CqlTemplate template = context.getBean(CqlTemplate.class);
		Session session = context.getBean(Session.class);

		template.execute("CREATE TABLE if not exists test (id uuid primary key, event text)");

		for (int i = 0; i < 10; i++) {
			template.execute("INSERT INTO test (id, event) VALUES (?,?)", UUID.randomUUID(),
					"event number: " + (int) (1000 * Math.random()));
		}
		template.query("select * From test", new RowMapper<Void>() {

			public Void mapRow(Row row, int rowNum) {
				System.out.println("id: " + row.getUUID("id") + " , event: " + row.getString("event"));
				return null;
			}

		});
		template.execute("drop table test");

		CreateTableSpecification createTableSpecification = CreateTableSpecification.createTable("test2")
				.partitionKeyColumn("id_dept", DataType.bigint()).clusteredKeyColumn("name", DataType.text())
				.column("salary", DataType.decimal());

		template.execute(CreateTableCqlGenerator.toCql(createTableSpecification));

		PreparedStatement prepare = session.prepare("INSERT INTO test2(id_dept, name, salary) VALUES (?,?,?)");
		for (int i = 0; i < 10; i++) {
			BoundStatement bind = prepare.bind((long) (3 * Math.random()) + 1,
					"clovek" + (long) (10000 * Math.random()) + 1,
					new BigDecimal(Math.random() * 5000).setScale(2, RoundingMode.CEILING));
			template.execute(bind);
		}
		// template.execute("drop table test2");

		BatchStatement batchStatement = new BatchStatement();
		prepare = session.prepare("INSERT INTO test2(id_dept, name, salary) VALUES (?,?,?)");
		for (int i = 0; i < 10; i++) {
			BoundStatement bind = prepare.bind((long) (3 * Math.random()) + 1,
					"clovek" + (long) (10000 * Math.random()) + 1,
					new BigDecimal(Math.random() * 5000).setScale(2, RoundingMode.CEILING));
			batchStatement.add(bind);
		}
		template.execute(batchStatement);
		
		template.query("SELECT * FROM test2", new ResultSetExtractor<Void>() {

			public Void extractData(ResultSet resultSet) throws DriverException, DataAccessException {
				resultSet.forEach(r -> {
					System.out.println("dept: " + r.getLong("id_dept") + " name: " + r.getString("name") + " salary: "
							+ r.getDecimal("salary"));
				});
				return null;
			}
		});
		DropTableSpecification dropSpec = DropTableSpecification.dropTable("test2");
		//template.execute(DropTableCqlGenerator.toCql(dropSpec));
		session.getCluster().close();
		context.close();
	}
}
