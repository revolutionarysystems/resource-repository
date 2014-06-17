package uk.co.revsys.resource.repository.classpath;

import java.io.File;
import java.io.IOException;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import uk.co.revsys.resource.repository.LocalDiskResourceRepository;

public class ClasspathResourceRepository extends LocalDiskResourceRepository implements ResourceLoaderAware{

    private ResourceLoader resourceLoader;

    public ClasspathResourceRepository(File repositoryBase) {
        super(repositoryBase);
    }

    @Override
    public void setResourceLoader(ResourceLoader rl) {
        this.resourceLoader = rl;
    }

    @Override
    public File getFile(String path) throws IOException {
        return resourceLoader.getResource("classpath:" + getRepositoryBase() + "/" + path).getFile();
    }

}
