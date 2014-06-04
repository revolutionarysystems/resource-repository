package uk.co.revsys.resource.repository.provider;

import java.io.IOException;
import java.io.InputStream;
import uk.co.revsys.resource.repository.ResourceRepository;
import uk.co.revsys.resource.repository.model.Directory;
import uk.co.revsys.resource.repository.model.Resource;

public class ResourceProvider {

    private final ResourceRepository resourceRepository;
    private final String path;
    private final ResourceHandler resourceHandler;

    public ResourceProvider(ResourceRepository resourceRepository, String path, ResourceHandler resourceHandler) {
        this.resourceRepository = resourceRepository;
        this.path = path;
        this.resourceHandler = resourceHandler;
    }

    public void refresh() throws IOException {
        refresh(this.path);
    }

    private void refresh(String path) throws IOException {
        for (Resource resource : resourceRepository.listResources(path)) {
            if (resourceHandler.canHandle(resource)) {
                InputStream contents = resourceRepository.read(resource);
                resourceHandler.handle(resource, contents);
            }
        }
        for (Directory directory : resourceRepository.listDirectories(path)) {
            refresh(directory.getPath() + "/" + directory.getName());
        }
    }
}
