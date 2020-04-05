package sk.upjs.neo4j_repository;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.config.Configuration.Builder;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "sk.upjs.neo4j_repository")
public class Neo4jConfig {

	public static final String LOCAL_STORE_DIR = "lokalnedb/1/embedded";
	public static final String URI = "bolt://nosql.gursky.sk:7687";
	public static final String USER = "neo4j";
	public static final String PASSWORD = "ls90zxcnm89fs46p6d7a1jn";
	public static final boolean LOCAL_DATABASE = false;

	@Bean
	public GraphDatabaseService getGraphDb() {
		GraphDatabaseFactory graphDatabaseFactory = new GraphDatabaseFactory();
		return graphDatabaseFactory.newEmbeddedDatabase(new File(LOCAL_STORE_DIR));
	}

	@Bean
	Configuration getConfiguration() {
		Builder builder = new Builder();
		return builder.uri(URI).credentials(USER, PASSWORD).build();
	}

	@Bean
	public SessionFactory getSessionFactory(Configuration config, GraphDatabaseService graphDatabaseService) {
		if (LOCAL_DATABASE) {
			return new SessionFactory(new EmbeddedDriver(graphDatabaseService, config), "sk.upjs.neo4j_repository");
		}
		return new SessionFactory(config, "sk.upjs.neo4j_repository");
	}
}
