package uk.co.revsys.resource.repository.model;

import java.util.Date;

public class Resource extends RepositoryItem{

	private Date lastModified;
	private long size;
	
	public Resource(String path, String name) {
		super(path, name);
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}
	
}
