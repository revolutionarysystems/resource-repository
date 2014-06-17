package uk.co.revsys.resource.repository.provider;

import java.io.FileNotFoundException;
import uk.co.revsys.resource.repository.provider.handler.ResourceHandler;
import uk.co.revsys.resource.repository.provider.filter.ResourceFilter;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import org.apache.commons.io.filefilter.FileFileFilter;
import uk.co.revsys.resource.repository.ResourceRepository;
import uk.co.revsys.resource.repository.model.Directory;
import uk.co.revsys.resource.repository.model.Resource;
import uk.co.revsys.resource.repository.provider.handler.FilteringResourceHandler;
import uk.co.revsys.resource.repository.provider.handler.StreamAwareResourceHandler;

public class ResourceProvider implements ServletContextAware {

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

    @Override
    public void setServletContext(ServletContext servletContext) {
        if (handler instanceof ServletContextAware) {
            ((ServletContextAware) handler).setServletContext(servletContext);
        }
    }

    public void refresh() throws IOException {
        refresh(this.path);
    }

    private void refresh(String path) throws IOException {
        if (handler instanceof StreamAwareResourceHandler) {
            ((StreamAwareResourceHandler) handler).newStream();
        }
        try {
            System.out.println("refreshing " + path);
            for (Resource resource : resourceRepository.listResources(path)) {
                System.out.println("resource = " + resource.getPath() + "/" + resource.getName());
                if (filter.accept(resource)) {
                    InputStream contents = resourceRepository.read(resource);
                    handler.handle(this.path, resource, contents);
                }
            }
            for (Directory directory : resourceRepository.listDirectories(path)) {
                System.out.println("directory = " + directory.getPath() + "/" + directory.getName());
                refresh(directory.getPath() + "/" + directory.getName());
            }
        }catch(FileNotFoundException ex){
            // Nothing to refresh
        }
        if (handler instanceof StreamAwareResourceHandler) {
            ((StreamAwareResourceHandler) handler).endOfStream();
        }
    }
}
