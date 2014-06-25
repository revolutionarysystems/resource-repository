package uk.co.revsys.resource.repository;

import java.io.File;

public class LocalDiskResourceRepositoryBuilder implements ResourceRepositoryBuilder{

    private final File repositoryBase;

    public LocalDiskResourceRepositoryBuilder(File repositoryBase) {
        this.repositoryBase = repositoryBase;
    }

    @Override
    public ResourceRepository build() {
        return new LocalDiskResourceRepository(repositoryBase);
    }
    
    
}
