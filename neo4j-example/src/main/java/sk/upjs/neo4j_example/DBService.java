package sk.upjs.neo4j_example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.io.fs.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;

import sk.upjs.nosql_data_source.entity.Download;
import sk.upjs.nosql_data_source.entity.Page;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.DownloadDao;

@Service
public class DBService {

	@Autowired
	GraphDatabaseService db;
	private DownloadDao downloadDao = DaoFactory.INSTANCE.getDownloadDao();
	private static Logger logger = LoggerFactory.getLogger(DBService.class);

	public static enum RelTypes implements RelationshipType {
		SEED_PAGE, LINKS_TO, CONTAINS
	}

	public static enum Labels implements Label {
		DOWNLOAD, PAGE
	}

	public DBService() {
		registerShutdownHook();
	}

	public void openAndFillDb() throws IOException {
		List<Download> downloads = downloadDao.getAllDownloads();
		Map<Long, Node> downloadMap = new HashMap<>();

		logger.info("creating indexes");

		try (Transaction tx = db.beginTx()) {
			db.schema().indexFor(Labels.DOWNLOAD).on("id").create();
			// db.schema().indexFor(Labels.PAGE).on("id").create();
			db.schema().indexFor(Labels.PAGE).on("url").create();
			tx.success();
		}

		try (Transaction tx = db.beginTx()) {
			db.schema().awaitIndexesOnline(30, TimeUnit.SECONDS);
			tx.success();
		}

		logger.info("finished creating indexes");

		Label downloadLabel = Labels.DOWNLOAD;
		Label pageLabel = Labels.PAGE;

		try (Transaction tx = db.beginTx()) {
			for (Download download : downloads) {
				Node dNode = db.createNode(downloadLabel);
				dNode.setProperty("id", download.getId());
				dNode.setProperty("url", download.getUrl());
				dNode.setProperty("startTime", download.getStartTime().toString());
				downloadMap.put(download.getId(), dNode);
			}
			tx.success();
		}

		logger.info("created download nodes");

		for (Download download : downloads) {

			Long seedPageID = download.getSeedPage().getId();
			Node dNode = downloadMap.get(download.getId());

			try (Transaction tx = db.beginTx()) {
				forpages: for (Page page : download.getPages()) {
					ResourceIterator<Node> nodesIterator = db.findNodes(Labels.PAGE, "url", page.getUrl());

					while (nodesIterator.hasNext()) {
						Node pageNode = nodesIterator.next();
						Node dn = pageNode.getSingleRelationship(RelTypes.CONTAINS, Direction.INCOMING).getStartNode();
						if (dn.getProperty("id").equals(dNode.getProperty("id"))) {
							if (page.getId() == seedPageID) {
								dNode.createRelationshipTo(pageNode, RelTypes.SEED_PAGE);
							}
							continue forpages;
						}
					}

					Node pNode = db.createNode(pageLabel);

					// pNode.setProperty("id", page.getId());
					pNode.setProperty("url", page.getUrl());
					pNode.setProperty("isDetailPage", page.isDetailPage());
					dNode.createRelationshipTo(pNode, RelTypes.CONTAINS);
					if (page.getId() == seedPageID) {
						dNode.createRelationshipTo(pNode, RelTypes.SEED_PAGE);
					}
				}
				tx.success();
			}
		}

		logger.info("created page nodes");

		for (Download download : downloads) {
			for (Page page : download.getPages()) {
				try (Transaction tx = db.beginTx()) {
					ResourceIterator<Node> nodeIterator = db.findNodes(Labels.PAGE, "url", page.getUrl());

					while (nodeIterator.hasNext()) {
						Node node1 = nodeIterator.next();

						Node dn = node1.getSingleRelationship(RelTypes.CONTAINS, Direction.INCOMING).getStartNode();
						if (dn.getProperty("id").equals(download.getId())) {
							for (Entry<String, Page> rel : page.getxPathToChildrenPages().entrySet()) {

								ResourceIterator<Node> nodeIterator2 = db.findNodes(Labels.PAGE, "url",
										rel.getValue().getUrl());

								while (nodeIterator2.hasNext()) {
									Node node2 = nodeIterator2.next();
									Node dn2 = node2.getSingleRelationship(RelTypes.CONTAINS, Direction.INCOMING)
											.getStartNode();
									if (dn2.getProperty("id").equals(download.getId())) {
										Relationship r = node1.createRelationshipTo(node2, RelTypes.LINKS_TO);

										r.setProperty("xPath", rel.getKey());
									}
								}
							}
							break;
						}
					}

					tx.success();
				}
			}
		}

	}

	public void shutDown() {
		db.shutdown();
	}

	public void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				db.shutdown();
			}
		});
	}

	public List<Node> getSeedNodes() {
		try (Transaction tx = db.beginTx()) {
			ResourceIterator<Node> dNodes = db.findNodes(Labels.DOWNLOAD);
			List<Node> seedNodes = new ArrayList<Node>();

			while (dNodes.hasNext()) {
				// seedNodes.add(dNodes.next());
				Node n = dNodes.next();
				seedNodes.add(n.getSingleRelationship(RelTypes.SEED_PAGE, Direction.OUTGOING).getEndNode());
			}

			tx.success();

			return seedNodes;
		}
	}

	public void printNodes(Collection<Node> nodes) {
		try (Transaction tx = db.beginTx()) {
			nodes.forEach(s -> System.out.println(s.getAllProperties()));
			tx.success();
		}
	}

	public void printDownloadTree(Node seedNode) {
		try (Transaction tx = db.beginTx()) {
			TraversalDescription traversalDescription = db.traversalDescription().breadthFirst()
					.relationships(RelTypes.LINKS_TO, Direction.OUTGOING);
			Traverser traverser = traversalDescription.traverse(seedNode);
			ResourceIterator<Path> paths = traverser.iterator();
			while (paths.hasNext()) {
				Path path = paths.next();
				for (int i = 0; i < path.length(); i++) {
					System.out.print("  ");
				}
				System.out.println(path.endNode().getProperty("url"));
			}
			tx.success();
		}
	}

	public void printShortestPathsToDetailPages(Node seedNode) {
		try (Transaction tx = db.beginTx()) {
			TraversalDescription traversalDescription = db.traversalDescription().breadthFirst()
					.relationships(RelTypes.LINKS_TO, Direction.OUTGOING).evaluator(new Evaluator() {

						@Override
						public Evaluation evaluate(Path path) {
							if (path.endNode().getProperty("isDetailPage", false).equals(true)) {
								return Evaluation.INCLUDE_AND_CONTINUE;
							}
							return Evaluation.EXCLUDE_AND_CONTINUE;
						}
					});

			Traverser traverser = traversalDescription.traverse(seedNode);
			ResourceIterator<Path> paths = traverser.iterator();
			PathFinder<Path> finder = GraphAlgoFactory
					.shortestPath(PathExpanders.forTypeAndDirection(RelTypes.LINKS_TO, Direction.OUTGOING), 100);

			while (paths.hasNext()) {
				Path path = paths.next();
				Path shortestPath = finder.findSinglePath(seedNode, path.endNode());

				System.out.println(path.length() + "/" + shortestPath.length());
				
				for (int i = 0; i < path.length(); i++) {
					System.out.print("  ");
				}
				System.out.println(path.endNode().getProperty("url"));
			}

			tx.success();
		}
	}
}
