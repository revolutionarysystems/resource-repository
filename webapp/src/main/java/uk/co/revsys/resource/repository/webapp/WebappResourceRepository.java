package uk.co.revsys.resource.repository.webapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import uk.co.revsys.resource.repository.LocalDiskResourceRepository;
import uk.co.revsys.resource.repository.model.Directory;
import uk.co.revsys.resource.repository.model.Resource;

public class WebappResourceRepository extends LocalDiskResourceRepository implements ResourceLoaderAware{

    private ResourceLoader resourceLoader;
    private String repositoryBase;

    public WebappResourceRepository(String repositoryBase) {
        super(null);
        this.repositoryBase = repositoryBase;
    }

    @Override
    public void setResourceLoader(ResourceLoader rl) {
        this.resourceLoader = rl;
    }

    @Override
    public File getRepositoryBase() {
        try {
            return resourceLoader.getResource(repositoryBase).getFile();
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public void delete(Directory directory) throws IOException {
        throw new AccessDeniedException(directory.getFullPath());
    }

    @Override
    public void delete(Resource resource) throws IOException {
        throw new AccessDeniedException(resource.getFullPath());
    }

    @Override
    public void write(Resource resource, InputStream inputStream) throws IOException {
        throw new AccessDeniedException(resource.getFullPath());
    }

}
