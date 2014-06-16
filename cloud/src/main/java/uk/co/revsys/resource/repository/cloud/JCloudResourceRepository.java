package uk.co.revsys.resource.repository.cloud;

import uk.co.revsys.resource.repository.model.Directory;
import uk.co.revsys.resource.repository.model.RepositoryItem;
import uk.co.revsys.resource.repository.model.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.blobstore.options.ListContainerOptions;
import uk.co.revsys.resource.repository.ResourceRepository;

public class JCloudResourceRepository implements ResourceRepository {

	private final BlobStore blobStore;
	private final String container;

	public JCloudResourceRepository(BlobStore blobStore, String container) {
		this.blobStore = blobStore;
		this.container = container;
	}

	@Override
	public void write(Resource resource, final InputStream inputStream) throws IOException {
		byte[] content = IOUtils.toByteArray(inputStream);
		Blob blob = blobStore.blobBuilder(resource.getPath() + "/" + resource.getName()).payload(content).contentLength(content.length).build();
		blobStore.putBlob(container, blob);
	}

	@Override
	public InputStream read(Resource resource) throws IOException {
		Blob blob = blobStore.getBlob(container, resource.getPath() + "/" + resource.getName());
		return blob.getPayload().getInput();
	}

	@Override
	public void delete(Resource resource) throws IOException {
		blobStore.removeBlob(container, resource.getPath() + "/" + resource.getName());
	}

	@Override
	public void delete(Directory directory) throws IOException {
		blobStore.clearContainer(container, new ListContainerOptions().inDirectory(directory.getPath() + "/" + directory.getName()).recursive());
	}

	@Override
	public List<RepositoryItem> list(String path) throws IOException {
		List<RepositoryItem> resources = new LinkedList<RepositoryItem>();
		ListContainerOptions listContainerOptions = new ListContainerOptions();
		if (!path.isEmpty()) {
			listContainerOptions.inDirectory(path);
		};
		PageSet<? extends StorageMetadata> list = blobStore.list(container, listContainerOptions);
		for (StorageMetadata item : list) {
			if (item.getType().equals(StorageType.BLOB)) {
				String fileName = item.getName();
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				Resource resource = new Resource(path, fileName);
				resource.setLastModified(item.getLastModified());
				resources.add(resource);
			} else if (item.getType().equals(StorageType.RELATIVE_PATH)) {
				String fileName = item.getName();
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				Directory resource = new Directory(path, fileName);
				resources.add(resource);
			}
		}
		return resources;
	}

	@Override
	public List<Directory> listDirectories(String path) throws IOException {
        System.out.println("listDirectories: " + path);
		List<Directory> directories = new LinkedList<Directory>();
		ListContainerOptions listContainerOptions = new ListContainerOptions();
		if (!path.isEmpty()) {
			listContainerOptions.inDirectory(path);
		};
		PageSet<? extends StorageMetadata> list = blobStore.list(container, listContainerOptions);
		for (StorageMetadata item : list) {
			if (item.getType().equals(StorageType.RELATIVE_PATH)) {
				String dirName = item.getName();
				dirName = dirName.substring(dirName.lastIndexOf("/") + 1);
				Directory directory = new Directory(path, dirName);
				directories.add(directory);
			}
		}
		return directories;
	}

	@Override
	public List<Resource> listResources(String path) throws IOException {
        System.out.println("listResources: " + path);
		List<Resource> resources = new LinkedList<Resource>();
		ListContainerOptions listContainerOptions = new ListContainerOptions();
		if (!path.isEmpty()) {
			listContainerOptions.inDirectory(path);
		};
		PageSet<? extends StorageMetadata> list = blobStore.list(container, listContainerOptions);
		for (StorageMetadata item : list) {
			if (item.getType().equals(StorageType.BLOB)) {
				String fileName = item.getName();
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				Resource resource = new Resource(path, fileName);
				resource.setLastModified(item.getLastModified());
				resource.setSize(blobStore.getBlob(container, item.getName()).getPayload().getContentMetadata().getContentLength());
				resources.add(resource);
			}
		}
		return resources;
	}

}
