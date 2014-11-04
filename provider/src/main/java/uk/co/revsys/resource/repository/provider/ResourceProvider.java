package uk.co.revsys.resource.repository.provider;

import java.io.FileNotFoundException;
import uk.co.revsys.resource.repository.provider.handler.ResourceHandler;
import uk.co.revsys.resource.repository.provider.filter.ResourceFilter;
import java.io.IOException;
import java.io.InputStream;
import uk.co.revsys.resource.repository.ResourceRepository;
import uk.co.revsys.resource.repository.model.Directory;
import uk.co.revsys.resource.repository.model.Resource;
import uk.co.revsys.resource.repository.provider.handler.FilteringResourceHandler;
import uk.co.revsys.resource.repository.provider.handler.StreamAwareResourceHandler;

public class ResourceProvider {

    private final ResourceRepository resourceRepository;
    private final String path;
    private final ResourceFilter filter;
    private final ResourceHandler handler;

    public ResourceProvider(ResourceRepository resourceRepository, String path, ResourceFilter filter, ResourceHandler handler) {
        this.resourceRepository = resourceRepository;
        this.path = path;
        this.filter = filter;
        this.handler = handler;
    }

    public ResourceProvider(ResourceRepository resourceRepository, String path, FilteringResourceHandler handler) {
        this(resourceRepository, path, handler, handler);
    }

    public void refresh() throws IOException {
        refresh(this.path, true);
    }

    private void refresh(String path, boolean testIfResource) throws IOException {
        if (handler instanceof StreamAwareResourceHandler) {
            ((StreamAwareResourceHandler) handler).newStream();
        }
        System.out.println("refreshing " + path);
        boolean itemFound = false;
        try {
            for (Resource resource : resourceRepository.listResources(path)) {
                System.out.println("resource = " + resource.getPath() + "/" + resource.getName());
                itemFound = true;
                if (filter.accept(resource)) {
                    InputStream contents = resourceRepository.read(resource);
                    handler.handle(this.path, resource, contents);
                }
            }
            for (Directory directory : resourceRepository.listDirectories(path)) {
                itemFound = true;
                System.out.println("directory = " + directory.getPath() + "/" + directory.getName());
                refresh(directory.getPath() + "/" + directory.getName(), false);
            }
        } catch (FileNotFoundException ex) {
            // Nothing to refresh
        }
        if (!itemFound && testIfResource) {
            try {
                String name = path;
                String thisPath = "";
                if(path.indexOf("/") > -1){
                    name = path.substring(path.lastIndexOf("/") + 1);
                    thisPath = path.substring(0, path.lastIndexOf("/"));
                }
                Resource resource = new Resource(thisPath, name);
                if (filter.accept(resource)) {
                    InputStream contents = resourceRepository.read(resource);
                    handler.handle(this.path, resource, contents);
                }
            } catch (FileNotFoundException ex) {
                // Nothing to refresh
            }
        }
        if (handler instanceof StreamAwareResourceHandler) {
            ((StreamAwareResourceHandler) handler).endOfStream();
        }
    }

    public ResourceRepository getResourceRepository() {
        return resourceRepository;
    }

    public String getPath() {
        return path;
    }

    public ResourceFilter getFilter() {
        return filter;
    }

    public ResourceHandler getHandler() {
        return handler;
    }
}
