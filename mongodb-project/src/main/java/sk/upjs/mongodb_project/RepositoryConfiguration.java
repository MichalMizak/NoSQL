package sk.upjs.mongodb_project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = { "sk.upjs.mongodb_project" })
@ComponentScan(basePackages = "sk.upjs.mongodb_project")
public class RepositoryConfiguration {	

	@Bean
	public MongoClient mongoClient() {

		// cloud.mongodb.com free 512MB server. Mam malo miesta v PC.
		String uri = "mongodb+srv://testuser:testuser@cluster-nmig0.mongodb.net/test?retryWrites=true&w=majority";

		MongoClient mongoClient = MongoClients.create(uri);

		// MongoDatabase database = mongoClient.getDatabase("test");

		return mongoClient;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient(), "test");
		return mongoTemplate;
	}

}