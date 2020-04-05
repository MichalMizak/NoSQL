package sk.upjs.neo4j_repository;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import sk.upjs.neo4j_repository.entitity.CrawlService;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Neo4jConfig.class);
		CrawlService crawlService = context.getBean(CrawlService.class);
		crawlService.fillDb();
		crawlService.printDetailPages();
		context.close();
	}
}
