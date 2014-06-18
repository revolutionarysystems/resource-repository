package uk.co.revsys.resource.repository.provider.servlet;

import javax.servlet.ServletContext;
import uk.co.revsys.resource.repository.ResourceRepository;
import uk.co.revsys.resource.repository.provider.ResourceProvider;
import uk.co.revsys.resource.repository.provider.filter.ResourceFilter;
import uk.co.revsys.resource.repository.provider.handler.FilteringResourceHandler;
import uk.co.revsys.resource.repository.provider.handler.ResourceHandler;

public class ServletAwareResourceProvider extends ResourceProvider implements ServletContextAware{

    public ServletAwareResourceProvider(ResourceRepository resourceRepository, String path, ResourceFilter filter, ResourceHandler handler) {
        super(resourceRepository, path, filter, handler);
    }

    public ServletAwareResourceProvider(ResourceRepository resourceRepository, String path, FilteringResourceHandler handler) {
        super(resourceRepository, path, handler);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        ResourceHandler handler = getHandler();
        if (handler instanceof ServletContextAware) {
            ((ServletContextAware) handler).setServletContext(servletContext);
        }
    }

}
