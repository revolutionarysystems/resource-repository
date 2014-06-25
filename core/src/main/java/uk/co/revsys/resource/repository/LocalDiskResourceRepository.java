package uk.co.revsys.resource.repository;

import uk.co.revsys.resource.repository.model.Directory;
import uk.co.revsys.resource.repository.model.RepositoryItem;
import uk.co.revsys.resource.repository.model.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class LocalDiskResourceRepository implements ResourceRepository {

	private final File repositoryBase;

	public LocalDiskResourceRepository(File repositoryBase) {
		this.repositoryBase = repositoryBase;
	}

    public File getRepositoryBase() {
        return repositoryBase;
    }

	@Override
	public void write(Resource resource, InputStream inputStream) throws IOException {
		File directory = getFile(resource.getPath());
		File file = new File(directory, resource.getName());
		FileUtils.copyInputStreamToFile(inputStream, file);
	}

	@Override
	public InputStream read(Resource resource) throws IOException {
		File directory = getFile(resource.getPath());
		File file = new File(directory, resource.getName());
		return FileUtils.openInputStream(file);
	}

	@Override
	public void delete(Resource resource) throws IOException {
		File directory = getFile(resource.getPath());
		File file = new File(directory, resource.getName());
		FileUtils.forceDelete(file);
	}

	@Override
	public void delete(Directory directory) throws IOException {
		File parent = getFile(directory.getPath());
		File dir = new File(parent, directory.getName());
		FileUtils.deleteDirectory(dir);
	}

	@Override
	public List<RepositoryItem> list(String path) throws IOException {
		List<RepositoryItem> list = new LinkedList<RepositoryItem>();
		File directory = getFile(path);
		for (File file : directory.listFiles()) {
			RepositoryItem item;
			if (file.isDirectory()) {
				item = new Directory(path, file.getName());
			} else {
				item = new Resource(path, file.getName());
				Resource resource = (Resource)item;
				resource.setLastModified(new Date(file.lastModified()));
				resource.setSize(file.length());
			}
			list.add(item);
		}
		return list;
	}

	@Override
	public List<Directory> listDirectories(String path) throws IOException {
		List<Directory> list = new LinkedList<Directory>();
		File directory = getFile(path);
		for (File file : directory.listFiles()) {
			Directory item;
			if (file.isDirectory()) {
				item = new Directory(path, file.getName());
				list.add(item);
			}
		}
		return list;
	}

	@Override
	public List<Resource> listResources(String path) throws IOException {
		List<Resource> list = new LinkedList<Resource>();
		File directory = getFile(path);
		for (File file : directory.listFiles()) {
			Resource item;
			if (!file.isDirectory()) {
				item = new Resource(path, file.getName());
				Resource resource = (Resource)item;
				resource.setLastModified(new Date(file.lastModified()));
				resource.setSize(file.length());
				list.add(item);
			}
		}
		return list;
	}
    
    public File getFile(String path) throws IOException{
        return new File(repositoryBase, path);
    }

}
