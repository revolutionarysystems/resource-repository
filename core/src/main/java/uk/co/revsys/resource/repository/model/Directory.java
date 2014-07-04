package uk.co.revsys.resource.repository.model;

public class Directory extends RepositoryItem{

    public Directory(String name) {
        super(name);
    }

	public Directory(String path, String name) {
		super(path, name);
	}

	@Override
	public boolean isDirectory() {
		return true;
	}

}
