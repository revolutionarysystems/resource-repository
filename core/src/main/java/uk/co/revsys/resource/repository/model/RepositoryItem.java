package uk.co.revsys.resource.repository.model;

import java.util.Map;

public abstract class RepositoryItem {

	private String name;
	private String path;
	private Map<String, String> metadata;

	public RepositoryItem(String path, String name) {
		this.name = name;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
	
	public abstract boolean isDirectory();
	
}
