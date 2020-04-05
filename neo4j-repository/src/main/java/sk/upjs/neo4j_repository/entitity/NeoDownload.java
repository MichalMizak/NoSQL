package sk.upjs.neo4j_repository.entitity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import scala.collection.immutable.HashSet;
import sk.upjs.nosql_data_source.entity.Download;
import sk.upjs.nosql_data_source.entity.Page;

@NodeEntity(label = "download")
public class NeoDownload {

	@Id
	private Long id;

	@Property(name = "start_time")
	private LocalDateTime startTime;

	@Property(name = "end_time")
	private LocalDateTime endTime;

	@Property
	private boolean finished;

	@Property
	private String url;

	@Property
	private String country;

	@Property
	private String language;

	@Relationship(type = "SEED_PAGE")
	private NeoPage seedPage;

	@Relationship(type = "CONTAINS")
	private Collection<NeoPage> pages = new java.util.HashSet<>();

	public NeoDownload() {
	}

	public NeoDownload(Download download) {
		super();
		this.id = download.getId();
		this.startTime = download.getStartTime();
		this.endTime = download.getEndTime();
		this.finished = download.isFinished();
		this.url = download.getUrl();
		this.country = download.getCountry();
		this.language = download.getLanguage();

		Map<String, NeoPage> urlToPage = new HashMap<>();

		download.getPages().forEach(p -> {
			NeoPage neoPage = urlToPage.get(p.getUrl());
			if (neoPage == null) {
				neoPage = new NeoPage(p, this);
				urlToPage.put(p.getUrl(), neoPage);
				pages.add(neoPage);
			}
			if (download.getSeedPage().getUrl().equals(p.getUrl())) {
				this.seedPage = neoPage;
			}
		});

		download.getPages().forEach(p -> {
			Set<LinksTo> links = new java.util.HashSet<>();
			NeoPage startPAge = urlToPage.get(p.getUrl());

			p.getxPathToChildrenPages().entrySet().forEach(xpathEntry -> {
				NeoPage endPage = urlToPage.get(xpathEntry.getValue().getUrl());
				links.add(new LinksTo(startPAge, endPage, xpathEntry.getKey()));
			});
			startPAge.setxPathToChildrenPages(links);
		});
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public NeoPage getSeedPage() {
		return seedPage;
	}

	public void setSeedPage(NeoPage seedPage) {
		this.seedPage = seedPage;
	}

	public Collection<NeoPage> getPages() {
		return pages;
	}

	public void setPages(Collection<NeoPage> pages) {
		this.pages = pages;
	}

}
