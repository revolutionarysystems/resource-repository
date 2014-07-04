package uk.co.revsys.resource.repository.classpath;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import uk.co.revsys.resource.repository.LocalDiskResourceRepository;

public class ClasspathResourceRepository extends LocalDiskResourceRepository implements ResourceLoaderAware{

    private ResourceLoader resourceLoader;
    private String repositoryBase;

    public ClasspathResourceRepository(String repositoryBase) {
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
            return resourceLoader.getResource("classpath:" + repositoryBase).getFile();
        } catch (IOException ex) {
            return null;
        }
    }

}
