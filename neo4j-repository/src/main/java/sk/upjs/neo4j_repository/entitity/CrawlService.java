package sk.upjs.neo4j_repository.entitity;

import java.awt.List;
import java.util.Collection;
import java.util.HashMap;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.upjs.nosql_data_source.entity.Download;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.DownloadDao;

@Service
public class CrawlService {

	@Autowired
	private SessionFactory sessionFactory;

	public void fillDb() {
		DownloadDao downloadDao = DaoFactory.INSTANCE.getDownloadDao();

		java.util.List<Download> allDownloads = downloadDao.getAllDownloads();
		NeoDownload neoDownload = new NeoDownload(allDownloads.get(0));
		Session session = sessionFactory.openSession();
		session.save(neoDownload);
	}

	public void printDetailPages() {
		Session session = sessionFactory.openSession();

		String query = "MATCH (det:page {is_detail_page: true}) RETURN det";
		Result result = session.query(query, new HashMap<>());

		result.forEach(page -> {
			System.out.println(page);
		});
	}

	public void printShortestPathsToDetailPages() {
		String query = "MATCH (seed:page)<-[:SEED_PAGE]-(:download), (det:page{is_detail_page: true}), "
				+ "p = shortestPath((seed)-[:LINKS_TO]-(det)) RETURN nodes(p) AS spath";

		Session session = sessionFactory.openSession();
		Result result = session.query(query, new HashMap<>());

		result.queryResults().forEach(strObjEntry -> {
			Collection<NeoPage> pages = (Collection<NeoPage>) strObjEntry.get("spath");

			System.out.println("size: " + pages.size());

			pages.forEach(System.out::println);
		});
	}

}
