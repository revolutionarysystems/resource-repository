package uk.co.revsys.resource.repository.cloud;

import java.io.FileNotFoundException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.revsys.resource.repository.ResourceRepository;

public class JCloudResourceRepository implements ResourceRepository {

    private Logger LOGGER = LoggerFactory.getLogger(JCloudResourceRepository.class);
    
	private final BlobStore blobStore;
	private final String container;
    private final String baseDir;

	public JCloudResourceRepository(BlobStore blobStore, String container) {
		this.blobStore = blobStore;
		this.container = container;
        this.baseDir = "";
	}

    public JCloudResourceRepository(BlobStore blobStore, String container, String baseDir) {
        this.blobStore = blobStore;
        this.container = container;
        if(!baseDir.isEmpty() && !baseDir.endsWith("/")){
            baseDir = baseDir + "/";
        }
        this.baseDir = baseDir;
    }

	@Override
	public void write(Resource resource, final InputStream inputStream) throws IOException {
        LOGGER.debug("Writing resource: container = " + container + ", baseDir = " + baseDir + ", path = " + resource.getPath() + ", name = " + resource.getName());
		byte[] content = IOUtils.toByteArray(inputStream);
		Blob blob = blobStore.blobBuilder(baseDir + resource.getFullPath()).payload(content).contentLength(content.length).build();
		blobStore.putBlob(container, blob);
	}

	@Override
	public InputStream read(Resource resource) throws IOException {
        LOGGER.debug("Reading resource: container = " + container + ", baseDir = " + baseDir + ", path = " + resource.getPath() + ", name = " + resource.getName());
		Blob blob = blobStore.getBlob(container, baseDir + resource.getFullPath());
        if(blob == null){
            throw new FileNotFoundException(resource.getFullPath());
        }
		return blob.getPayload().getInput();
	}

	@Override
	public void delete(Resource resource) throws IOException {
        LOGGER.debug("Deleting resource: container = " + container + ", baseDir = " + baseDir + ", path = " + resource.getPath() + ", name = " + resource.getName());
		blobStore.removeBlob(container, baseDir + resource.getFullPath());
	}

	@Override
	public void delete(Directory directory) throws IOException {
        LOGGER.debug("Reading directory: container = " + container + ", baseDir = " + baseDir + ", path = " + directory.getPath() + ", name = " + directory.getName());
		blobStore.clearContainer(container, new ListContainerOptions().inDirectory(baseDir + directory.getPath() + "/" + directory.getName()).recursive());
	}

	@Override
	public List<RepositoryItem> list(String path) throws IOException {
        if(path.equals(".")){
            path = "/";
        }
        LOGGER.debug("Listing directory: container = " + container + ", baseDir = " + baseDir + ", path = " + path);
		List<RepositoryItem> resources = new LinkedList<RepositoryItem>();
		ListContainerOptions listContainerOptions = new ListContainerOptions();
		if (!path.isEmpty()) {
			listContainerOptions.inDirectory(baseDir + path);
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
        if(path.equals(".")){
            path = "/";
        }
        LOGGER.debug("Listing directories: container = " + container + ", baseDir = " + baseDir + ", path = " + path);
        System.out.println("listDirectories: " + path);
		List<Directory> directories = new LinkedList<Directory>();
		ListContainerOptions listContainerOptions = new ListContainerOptions();
		if (!path.isEmpty()) {
			listContainerOptions.inDirectory(baseDir + path);
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
        if(path.equals(".")){
            path = "/";
        }
        LOGGER.debug("Listing resources: container = " + container + ", baseDir = " + baseDir + ", path = " + path);
		List<Resource> resources = new LinkedList<Resource>();
		ListContainerOptions listContainerOptions = new ListContainerOptions();
		if (!path.isEmpty()) {
			listContainerOptions.inDirectory(baseDir + path);
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
